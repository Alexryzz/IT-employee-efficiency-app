package org.coursework.app.controller;

import lombok.RequiredArgsConstructor;
import org.coursework.app.dto.TaskRequest;
import org.coursework.app.entity.Task;
import org.coursework.app.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final TaskService taskService;

    @PostMapping("/create-task")
    public ResponseEntity<?> createTask(@RequestBody TaskRequest taskRequest){
        try{
            Task task = taskService.taskCreate(taskRequest);
            return ResponseEntity.ok(task);
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-task")
    public ResponseEntity<String> deleteTask(@RequestParam String title){
        taskService.deleteTask(title);
        return ResponseEntity.ok("Задача удалена успешно!");
    }

}
