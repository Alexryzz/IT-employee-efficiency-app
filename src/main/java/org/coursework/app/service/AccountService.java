package org.coursework.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.coursework.app.service.util.ValidatorUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final StatsRepository statsRepository;
    private final AllEmployeesStatsRepository allEmployeesStatsRepository;

    @Value("${admin.registration.secret-code}")
    private String adminSecretCode;

    public void registerAccount(RegisterRequest registerRequest) {
        if (accountRepository.existsByEmail(registerRequest.getEmail())) {
            log.warn("Account with email {} already exists", registerRequest.getEmail());
            throw new IllegalArgumentException("Email уже используется");
        }

        ValidatorUtil.validatePasswordMatch(registerRequest.getPassword(),
                registerRequest.getConfirmPassword());
        Account account = new Account();
        account.setEmail(registerRequest.getEmail());
        account.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        account.setRole(Role.ROLE_WORKER);

        log.info("Saving account");
        accountRepository.save(account);

        createStats(account);
    }

    public void registerAdmin(RegisterAdminRequest registerAdminRequest) {
        ValidatorUtil.validatePasswordMatch(registerAdminRequest.getPassword(),
                registerAdminRequest.getConfirmPassword());
        validateSecretCode(registerAdminRequest.getAdminCode());
        Account account = new Account();
        account.setEmail(registerAdminRequest.getEmail());
        account.setPassword(passwordEncoder.encode(registerAdminRequest.getPassword()));
        account.setRole(Role.ROLE_ADMIN);

        log.info("Saving admin account");
        accountRepository.save(account);
    }

    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Аккаунт не существует"));
    }

    public Account validateLoginRequest(LoginRequest loginRequest) {
        Account account = findByEmail(loginRequest.getEmail());
        if (!passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())) {
            log.warn("Passwords do not match");
            throw new IllegalArgumentException("Пароли не совпадают");
        }
        log.info("Validating login request");
        return account;
    }

    public void setWorkerGrade(long id, UpdateGradeRequest workerGrade) {
        Account account = getAccount(id);
        ValidatorUtil.validateSetGradeRequest(account);
        ValidatorUtil.validateNotAdminGrade(account);
        account.setGrade(workerGrade.getWorkerGrade());
        allEmployeesStatsRepository.incrementCount(workerGrade.getWorkerGrade());
        log.info("saving worker grade");
        accountRepository.save(account);
    }

    public void updateWorkerGrade(long id, UpdateGradeRequest workerGrade) {
        Account account = getAccount(id);
        ValidatorUtil.validateUpdateGradeRequest(account);
        ValidatorUtil.validateNotAdminGrade(account);

        if (ValidatorUtil.validateDifferentGrades(account, workerGrade)) {
            log.info("updating all employees stats");
            allEmployeesStatsRepository.decrementCount(account.getGrade());
            allEmployeesStatsRepository.incrementCount(workerGrade.getWorkerGrade());
            log.info("updating worker grade");
            account.setGrade(workerGrade.getWorkerGrade());
            accountRepository.save(account);
        } else {
            log.warn("Worker grade not saving");
        }
    }

    private void createStats(Account account) {
        Stats stats = new Stats();
        stats.setAccount(account);
        stats.setTimeEffectivity((short) 0);
        log.info("saving stats");
        statsRepository.save(stats);
    }

    private Account getAccount(long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Такой аккаунт не найден"));
    }

    private void validateSecretCode(String secretCode) {
        if (!secretCode.equals(adminSecretCode)) {
            log.warn("Secret code do not match");
            throw new IllegalArgumentException("Неверный код администратора!");
        }
    }
}
