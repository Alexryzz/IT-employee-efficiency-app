package org.coursework.app.controller;

import lombok.RequiredArgsConstructor;
import org.coursework.app.dto.TaskRequest;
import org.coursework.app.entity.Task;
import org.coursework.app.service.StatsService;
import org.coursework.app.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final TaskService taskService;
    private final StatsService statsService;

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

    @GetMapping("/worker-stats/{id}")
    public ResponseEntity<?> getWorkerStats(@PathVariable Long id){
        try{
            return ResponseEntity.ok(statsService.getStatsByAccountId(id));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
