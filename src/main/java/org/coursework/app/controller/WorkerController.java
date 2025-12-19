package org.coursework.app.controller;

import lombok.RequiredArgsConstructor;
import org.coursework.app.service.AccountService;
import org.coursework.app.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/worker")
public class WorkerController {
    private final AccountService accountService;
    private final TaskService taskService;

    @PatchMapping("/get-task")
    public ResponseEntity<?> getTask(@RequestParam String title, Principal principal){
        try {
            taskService.getTask(title, principal.getName());
            return ResponseEntity.ok("Задача: " + title + "\nПерешла к выполнению" );
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/task-completed")
    public ResponseEntity<?> taskComplete(@RequestParam String title, Principal principal){
        try{
            taskService.taskCompleted(title, principal.getName());
            return ResponseEntity.ok("Задача: "+ title + "\nВыполнена");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
