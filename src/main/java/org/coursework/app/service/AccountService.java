package org.coursework.app.service;

import lombok.RequiredArgsConstructor;
import org.coursework.app.dto.LoginRequest;
import org.coursework.app.dto.RegisterAdminRequest;
import org.coursework.app.dto.RegisterRequest;
import org.coursework.app.dto.UpdateGradeRequest;
import org.coursework.app.entity.Account;
import org.coursework.app.entity.AllEmployeesStats;
import org.coursework.app.entity.Stats;
import org.coursework.app.enums.Role;
import org.coursework.app.enums.WorkerGrade;
import org.coursework.app.enums.taskEnums.TaskType;
import org.coursework.app.repository.AccountRepository;
import org.coursework.app.repository.AllEmployeesStatsRepository;
import org.coursework.app.repository.StatsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final StatsRepository statsRepository;
    private final AllEmployeesStatsRepository allEmployeesStatsRepository;

    @Value("${admin.registration.secret-code}")
    private String adminSecretCode;

    public void registerAccount(RegisterRequest registerRequest) {
        if(accountRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email уже используется");
        }

        validatePasswordMatch(registerRequest.getPassword(),  registerRequest.getConfirmPassword());
        Account account = new Account();
        account.setEmail(registerRequest.getEmail());
        account.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        account.setRole(Role.ROLE_WORKER);

        accountRepository.save(account);
    }

    public void registerAdmin(RegisterAdminRequest registerAdminRequest) {
        validatePasswordMatch(registerAdminRequest.getPassword(),
                registerAdminRequest.getConfirmPassword());
        validateSecretCode(registerAdminRequest.getAdminCode());
        Account account = new Account();
        account.setEmail(registerAdminRequest.getEmail());
        account.setPassword(passwordEncoder.encode(registerAdminRequest.getPassword()));
        account.setRole(Role.ROLE_ADMIN);

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

    public Account validateLoginRequest(LoginRequest loginRequest) {
        Account account = accountRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Аккаунт не существует"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())) {
            throw new IllegalArgumentException("Пароли не совпадают");
        }

        return account;
    }

    public void setWorkerGrade(long id, UpdateGradeRequest workerGrade) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Такой аккаунт не найден"));
        if(account.getRole().equals(Role.ROLE_ADMIN)) {
            throw new IllegalArgumentException("Нельзя изменить уровень админа :)");
        }
        account.setGrade(workerGrade.getWorkerGrade());
        updateAllEmployeesStats(workerGrade.getWorkerGrade());
        accountRepository.save(account);
    }

    public Stats createStats(long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Аккаунт не найден"));
        Stats stats = new Stats();
        stats.setAccount(account);
        stats.setTimeEffectivity((short)0);
        return statsRepository.save(stats);
    }

    private void updateAllEmployeesStats(WorkerGrade workerGrade) {
        allEmployeesStatsRepository.increaseCountByGrade(workerGrade);
    }
}
