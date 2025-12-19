package org.coursework.app.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.coursework.app.entity.Account;
import org.coursework.app.entity.Stats;
import org.coursework.app.entity.Task;
import org.coursework.app.repository.AccountRepository;
import org.coursework.app.repository.StatsRepository;
import org.coursework.app.repository.TaskRepository;
import org.coursework.app.service.util.StatsUtil;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final AccountRepository accountRepository;
    private final TaskRepository taskRepository;
    private final StatsRepository statsRepository;

    public Stats getStatsByAccountId(long id) {
        Stats stats = new Stats();
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Аккаунт не найден"));

        List<Task> tasks = account.getTasks();

        stats.setAccount(account);
        stats.setTaskTypeRatio(StatsUtil.considerTypeEffectivity(tasks));
        stats.setTimeEffectivity(StatsUtil.considerTimeEffectivity(tasks.getLast()));
        return stats;
    }
}
