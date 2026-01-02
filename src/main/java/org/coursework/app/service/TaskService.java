package org.coursework.app.service;

import lombok.RequiredArgsConstructor;
import org.coursework.app.dto.TaskRequest;
import org.coursework.app.entity.Task;
import org.coursework.app.enums.taskEnums.TaskStatus;
import org.coursework.app.repository.AccountRepository;
import org.coursework.app.repository.TaskRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final AccountRepository accountRepository;

    public Task taskCreate(TaskRequest taskRequest){
        validateTaskMatch(taskRequest);

        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setTaskType(taskRequest.getTaskType());
        task.setTaskComplexity(taskRequest.getTaskComplexity());
        task.setTaskImportance(taskRequest.getTaskImportance());
        task.setTaskGetDate(LocalDate.now());
        task.setDeadline(taskRequest.getDeadline());
        task.setTaskStatus(TaskStatus.TO_DO);

        return taskRepository.save(task);
    }

    public void deleteTask(String title){
        Task task = taskRepository.findByTitle(title)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Задания с таким заголовком не существует"));
        taskRepository.delete(task);
    }

    public void getTask(String title, String workerEmail){
        Task task = taskRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("Задания с таким заголовком не существует"));
        task.setTaskStatus(TaskStatus.IN_PROGRESS);
        task.setAccount(
                accountRepository.findByEmail(workerEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("Аккаунт не найден"))
        );
        task.setTaskGetDate(LocalDate.now());

        taskRepository.save(task);
    }

    public void taskCompleted(String title, String workerEmail){

        Task task = taskRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("Задания с таким заголовком не существует"));
        task.setTaskStatus(TaskStatus.COMPLETED);
        task.setTaskDoneDate(LocalDate.now());

        taskRepository.save(task);
    }

    private void validateTaskMatch(TaskRequest taskRequest){
        if(taskRepository.existsByTitle(taskRequest.getTitle())){
           throw new IllegalArgumentException("Задача с таким заголовком уже существует");
        }
    }

}
