package org.coursework.app.controller;

import lombok.RequiredArgsConstructor;
import org.coursework.app.dto.LoginRequest;
import org.coursework.app.dto.RegisterAdminRequest;
import org.coursework.app.dto.RegisterRequest;
import org.coursework.app.entity.Account;
import org.coursework.app.jwt.models.JwtResponse;
import org.coursework.app.jwt.models.RefreshTokenRequest;
import org.coursework.app.jwt.util.JwtUtils;
import org.coursework.app.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {
    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> register (@RequestBody RegisterRequest registerRequest) {
        try{
            accountService.registerAccount(registerRequest);
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<?> registerAdmin (@RequestBody RegisterAdminRequest registerAdminRequest) {
        try{
            accountService.registerAdmin(registerAdminRequest);
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Account account = accountService.findByEmail(request.getEmail());

        String accessToken = jwtUtils.generateAccessToken(account);
        String refreshToken = jwtUtils.generateRefreshToken(account);

        JwtResponse response = JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .id(account.getId())
                .email(account.getEmail())
                .role(account.getRole())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@RequestBody RefreshTokenRequest request) {
        if (!jwtUtils.validateToken(request.getRefreshToken())) {
            throw new RuntimeException("Невалидный refresh токен");
        }

        String email = jwtUtils.getEmailFromToken(request.getRefreshToken());
        Account account = accountService.findByEmail(email);

        String newAccessToken = jwtUtils.generateAccessToken(account);
        String newRefreshToken = jwtUtils.generateRefreshToken(account);

        JwtResponse response = JwtResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .id(account.getId())
                .email(account.getEmail())
                .role(account.getRole())
                .build();

        return ResponseEntity.ok(response);
    }

}
