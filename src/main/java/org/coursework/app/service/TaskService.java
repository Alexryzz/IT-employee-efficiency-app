package org.coursework.app.service;

import lombok.RequiredArgsConstructor;
import org.coursework.app.dto.TaskRequest;
import org.coursework.app.entity.Task;
import org.coursework.app.enums.taskEnums.TaskStatus;
import org.coursework.app.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

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
        Task task = taskRepository.findByTitle(title);
        taskRepository.delete(task);
    }

    private void validateTaskMatch(TaskRequest taskRequest){
        if(taskRepository.existsByTitle(taskRequest.getTitle())){
           throw new IllegalArgumentException("Задача с таким заголовком уже существует");
        }
    }


}
