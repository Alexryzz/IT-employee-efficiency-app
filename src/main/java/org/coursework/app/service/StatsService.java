package org.coursework.app.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.coursework.app.entity.Account;
import org.coursework.app.entity.Stats;
import org.coursework.app.entity.Task;
import org.coursework.app.enums.taskEnums.TaskStatus;
import org.coursework.app.repository.AccountRepository;
import org.coursework.app.repository.StatsRepository;
import org.coursework.app.repository.TaskRepository;
import org.coursework.app.service.util.StatsUtil;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsService {
    private final AccountRepository accountRepository;
    private final StatsRepository statsRepository;

    public Stats updateStatsByAccountEmail(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Аккаунт не найден"));
        Stats stats = account.getStats();
        List<Task> tasks = getCompletedTasksByAccount(account);
        stats.setTaskTypeRatio(StatsUtil.considerTypeEffectivity(tasks));
        stats.setTimeEffectivity(StatsUtil.considerTimeEffectivity(stats.getTimeEffectivity(), tasks.size() ,tasks.getLast()));
        log.info("updating stats");
        return statsRepository.save(stats);
    }

    private List<Task> getCompletedTasksByAccount(Account account) {
        List<Task> tasks = account.getWorkTasks();
        List<Task> completedTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getTaskStatus().equals(TaskStatus.COMPLETED)){
                completedTasks.add(task);
            }
        }
        log.info("getting completed tasks");
        return completedTasks;
    }

    
}
