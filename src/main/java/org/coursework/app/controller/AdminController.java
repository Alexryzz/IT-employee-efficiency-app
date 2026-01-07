package org.coursework.app.controller;

import lombok.RequiredArgsConstructor;
import org.coursework.app.dto.UpdateGradeRequest;
import org.coursework.app.dto.taskDto.TaskCreatedResponse;
import org.coursework.app.dto.taskDto.TaskRequest;
import org.coursework.app.enums.WorkerGrade;
import org.coursework.app.jwt.CustomPrincipal;
import org.coursework.app.service.AccountService;
import org.coursework.app.service.StatsService;
import org.coursework.app.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final TaskService taskService;
    private final StatsService statsService;
    private final AccountService accountService;

    @PostMapping("/task")
    public ResponseEntity<?> createTask(@RequestBody TaskRequest taskRequest,
                                        @AuthenticationPrincipal CustomPrincipal  customPrincipal){
            Long adminId = customPrincipal.getId();
            TaskCreatedResponse response = taskService.convertToTaskCreatedResponse(
                    taskService.taskCreate(taskRequest, adminId));
            return ResponseEntity.ok(response);
    }

    @DeleteMapping("/task/{title}")
    public ResponseEntity<String> deleteTask(@PathVariable String title){
        taskService.deleteTask(title);
        return ResponseEntity.ok("Задача удалена успешно!");
    }

//    @GetMapping("/worker-stats/{id}")
//    public ResponseEntity<?> getWorkerStats(@PathVariable Long id){
//        try{
//            return ResponseEntity.ok(statsService.getStatsByAccountId(id));
//        }
//        catch(Exception e){
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    @PatchMapping("/worker-grade/{id}")
    public ResponseEntity<?> updateWorkerGrade(@PathVariable Long id,
                                               @RequestBody UpdateGradeRequest workerGrade){
        accountService.setWorkerGrade(id, workerGrade);
        accountService.createStats(id);
        return ResponseEntity.ok("Уровень работника изменен.");
    }
}
