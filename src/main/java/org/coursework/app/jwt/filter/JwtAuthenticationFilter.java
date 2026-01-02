package org.coursework.app.jwt.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.coursework.app.enums.Role;
import org.coursework.app.jwt.CustomPrincipal;
import org.coursework.app.jwt.util.JwtUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (jwtUtils.validateToken(token)) {
            if (!jwtUtils.validateExpiration(token)) {
                throw new CredentialsExpiredException("Expired JWT token");
            }

            Claims payload = jwtUtils.getClaimsFromToken(token).getPayload();
            String role = payload.get("role", String.class);
            CustomPrincipal customPrincipal = new CustomPrincipal(
                    payload.get("id", Long.class),
                    payload.getSubject(),
                    Role.valueOf(role.toUpperCase())
                    );

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            customPrincipal,
                            token,
                            customPrincipal.getAuthorities()
                    );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
            throw new InternalAuthenticationServiceException("Невалидный токен");
        }

        filterChain.doFilter(request, response);
    }
}
