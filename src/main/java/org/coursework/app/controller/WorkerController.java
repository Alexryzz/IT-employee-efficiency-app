package org.coursework.app.controller;

import lombok.RequiredArgsConstructor;
import org.coursework.app.jwt.CustomPrincipal;
import org.coursework.app.service.StatsService;
import org.coursework.app.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/worker")
public class WorkerController {
    private final TaskService taskService;
    private final StatsService statsService;

    @PatchMapping("/task-start/{title}")
    public ResponseEntity<?> getTask(@AuthenticationPrincipal CustomPrincipal principal, @PathVariable String title) {
        taskService.getTask(title, principal.getEmail());
        return ResponseEntity.ok("Задача: " + title + "\nПерешла к выполнению");

    }

    @PatchMapping("/task-end/{title}")
    public ResponseEntity<?> taskComplete(@PathVariable String title,
                                          @AuthenticationPrincipal CustomPrincipal principal) {
        taskService.taskCompleted(title);
        statsService.updateStatsByAccountEmail(principal.getEmail());
        return ResponseEntity.ok("Задача: " + title + "\nВыполнена");
    }
}
