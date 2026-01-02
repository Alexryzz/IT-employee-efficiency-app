package org.coursework.app.controller;

import lombok.RequiredArgsConstructor;
import org.coursework.app.service.AccountService;
import org.coursework.app.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/worker")
public class WorkerController {
    private final AccountService accountService;
    private final TaskService taskService;

    @PatchMapping("/get-task")
    public ResponseEntity<?> getTask(@AuthenticationPrincipal UserDetails principal , @RequestParam String title){
        try {
            taskService.getTask(title, principal.getUsername());
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

    @GetMapping("/test")
    public ResponseEntity<String> test(@AuthenticationPrincipal UserDetails principal){
        System.out.println(SecurityContextHolder.getContext());
        return ResponseEntity.ok(principal.getUsername());
    }
}
