package org.coursework.app.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.coursework.app.dto.AccountDto.SimpleAccountDto;
import org.coursework.app.dto.taskDto.TaskCreatedResponse;
import org.coursework.app.dto.taskDto.TaskRequest;
import org.coursework.app.entity.Account;
import org.coursework.app.entity.Task;
import org.coursework.app.enums.taskEnums.TaskStatus;
import org.coursework.app.repository.AccountRepository;
import org.coursework.app.repository.TaskRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private final AccountRepository accountRepository;

    public Task taskCreate(TaskRequest taskRequest, Long adminId) {
        validateTaskMatch(taskRequest);

        Account admin = accountRepository.findById(adminId).orElseThrow(() ->
                new UsernameNotFoundException("Admin id not found"));

        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setTaskType(taskRequest.getTaskType());
        task.setTaskComplexity(taskRequest.getTaskComplexity());
        task.setTaskImportance(taskRequest.getTaskImportance());
        task.setTaskCreatedDate(LocalDateTime.now());
        task.setDeadline(taskRequest.getDeadline());
        task.setTaskStatus(TaskStatus.TO_DO);
        task.setAdmin(admin);
        log.info("saving task");
        return taskRepository.save(task);
    }

    public TaskCreatedResponse convertToTaskCreatedResponse(Task task) {
        TaskCreatedResponse response = new TaskCreatedResponse();
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setTaskType(task.getTaskType());
        response.setTaskComplexity(task.getTaskComplexity());
        response.setTaskImportance(task.getTaskImportance());
        response.setDeadline(task.getDeadline());
        response.setTaskCreatedDate(task.getTaskCreatedDate());
        response.setTaskStatus(task.getTaskStatus());
        response.setAdmin(new SimpleAccountDto(
                task.getAdmin().getId(),
                task.getAdmin().getEmail(),
                task.getAdmin().getRole()
        ));
        log.info("creating task response");
        return response;
    }

    public void deleteTask(String title) {
        Task task = taskRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Задания с таким заголовком не существует"));
        log.info("deleting task");
        taskRepository.delete(task);
    }

    public void getTask(String title, String workerEmail) {
        Account worker = accountRepository.findByEmail(workerEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Аккаунт не найден"));
        if (worker.getGrade() != null) {
            Task task = taskRepository.findByTitle(title)
                    .orElseThrow(() -> new IllegalArgumentException("Задания с таким заголовком не существует"));

            task.setWorker(worker);
            task.setTaskGetDate(LocalDateTime.now());
            task.setTaskStatus(TaskStatus.IN_PROGRESS);
            log.info("getting task");
            taskRepository.save(task);
        } else {
            log.warn("unable to get task");
            throw new BadCredentialsException("У вас не достаточно полномочий.");
        }
    }

    public void taskCompleted(String title) {

        Task task = taskRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("Задания с таким заголовком не существует"));
        if (task.getTaskStatus().equals(TaskStatus.IN_PROGRESS)) {
            task.setTaskStatus(TaskStatus.COMPLETED);
            task.setTaskDoneDate(LocalDateTime.now());
            log.info("completing task");
            taskRepository.save(task);
        } else {
            log.warn("task was not under development");
            throw new IllegalArgumentException("Задание не было взято на разработку");
        }
    }

    private void validateTaskMatch(TaskRequest taskRequest) {
        if (taskRepository.existsByTitle(taskRequest.getTitle())) {
            log.warn("task with this title already exists");
            throw new IllegalArgumentException("Задача с таким заголовком уже существует");
        }
    }

}
