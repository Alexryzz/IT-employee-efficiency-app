package org.coursework.app.service;

import lombok.RequiredArgsConstructor;
import org.coursework.app.dto.RegisterAdminRequest;
import org.coursework.app.dto.RegisterRequest;
import org.coursework.app.entity.Account;
import org.coursework.app.enums.Role;
import org.coursework.app.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.registration.secret-code}")
    private String adminSecretCode;

    public void registerAccount(RegisterRequest registerRequest) {
        if(accountRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email уже используется");
        }

        validatePasswordMatch(registerRequest.getPassword(),  registerRequest.getConfirmPassword());
        Account account = new Account();
        account.setEmail(registerRequest.getEmail());
        account.setPassword(registerRequest.getPassword());
        account.setRole(Role.WORKER);

        accountRepository.save(account);
    }

    public void registerAdmin(RegisterAdminRequest registerAdminRequest) {
        validatePasswordMatch(registerAdminRequest.getPassword(),
                registerAdminRequest.getConfirmPassword());
        validateSecretCode(registerAdminRequest.getAdminCode());
        Account account = new Account();
        account.setEmail(registerAdminRequest.getEmail());
        account.setPassword(passwordEncoder.encode(registerAdminRequest.getPassword()));
        account.setRole(Role.ADMIN);

        accountRepository.save(account);
    }

    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Аккаунт не существует"));
    }

    private void validatePasswordMatch(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Пароли не совпадают!");
        }
    }

    private void validateSecretCode(String secretCode) {
        if (!secretCode.equals(adminSecretCode)) {
            throw new IllegalArgumentException("Неверный код администратора!");
        }
    }
}
