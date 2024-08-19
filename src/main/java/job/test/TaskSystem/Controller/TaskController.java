package job.test.TaskSystem.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import job.test.TaskSystem.Model.TaskDTO;
import job.test.TaskSystem.Model.TaskStatus;
import job.test.TaskSystem.Model.UserDTO;
import job.test.TaskSystem.Service.JwtService;
import job.test.TaskSystem.Service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@Tag(name="Task Controller", description="Controller present API to interact with tasks")
public class TaskController {
    private final TaskService taskService;
    private final JwtService jwtService;

    @Operation(summary = "Получение всех задач текущего пользователя")
    @GetMapping()
    public ResponseEntity<Page<TaskDTO>> tasks(@RequestHeader("Authorization") String authorizationHeader, Pageable pageable) {
        String token = extractJwtToken(authorizationHeader);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Page.empty());

        UserDTO user = jwtService.extractUser(token);

        return ResponseEntity.ok(taskService.getAllAuthorTasks(user, pageable));
    }

    @Operation(summary = "Получение всех задач пользователя по email")
    @GetMapping("/{email}")
    public ResponseEntity<Page<TaskDTO>> getTasksByEmails(@PathVariable String email, Pageable pageable) {
        return ResponseEntity.ok(taskService.getAllUserTasks(email, pageable));
    }

    @Operation(summary = "Получение всех задач пользователя по email с определённым статусом")
    @GetMapping("/{email}/status")
    public ResponseEntity<Page<TaskDTO>> getTasksByEmailAndStatus(@PathVariable String email, @RequestParam TaskStatus status, Pageable pageable) {
        return ResponseEntity.ok(taskService.getAllUserTasksByStatus(email, status, pageable));
    }

    @Operation(summary = "Добавить задачу текущему пользователю")
    @PostMapping()
    public ResponseEntity<TaskDTO> addTask(@RequestHeader("Authorization") String authorizationHeader, @RequestParam String title, @RequestParam String comment) {
        String token = extractJwtToken(authorizationHeader);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        UserDTO user = jwtService.extractUser(token);

        return ResponseEntity.ok(taskService.addTask(user, title, comment));
    }

    @Operation(summary = "Изменить заголовок или комментарий к задаче по её id")
    @PutMapping()
    public ResponseEntity<TaskDTO> editTask(@RequestHeader("Authorization") String authorizationHeader, @RequestParam Long taskID, @RequestParam String title, @RequestParam String comment) {
        String token = extractJwtToken(authorizationHeader);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        UserDTO user = jwtService.extractUser(token);

        return ResponseEntity.ok(taskService.editTask(user, taskID, title, comment));
    }

    @Operation(summary = "Удалить задачу по её id")
    @DeleteMapping()
    public ResponseEntity<String> deleteTask(@RequestHeader("Authorization") String authorizationHeader, @RequestParam Long taskID) {
        String token = extractJwtToken(authorizationHeader);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        UserDTO user = jwtService.extractUser(token);

        taskService.deleteTask(user, taskID);
        return ResponseEntity.ok("Task deleted");
    }

    @Operation(summary = "Изменить статус задачи по её id")
    @PutMapping("/status")
    public ResponseEntity<TaskDTO> updateTaskStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestParam Long taskID, @RequestParam TaskStatus status) {
        String token = extractJwtToken(authorizationHeader);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        UserDTO user = jwtService.extractUser(token);

        return ResponseEntity.ok(taskService.changeStatus(taskID, status, user));
    }


    @Operation(summary = "Добавить исполнителя задачи по её id")
    @PutMapping("/worker")
    public ResponseEntity<TaskDTO> addWorker(@RequestHeader("Authorization") String authorizationHeader, @RequestParam String email, @RequestParam Long taskID) {
        String token = extractJwtToken(authorizationHeader);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        UserDTO user = jwtService.extractUser(token);

        return ResponseEntity.ok(taskService.addWorker(taskID, email, user));
    }

    @Operation(summary = "Убрать исполнителя задачи по её id")
    @DeleteMapping("/worker")
    public ResponseEntity<TaskDTO> removeWorker(@RequestHeader("Authorization") String authorizationHeader, @RequestParam String email, @RequestParam Long taskID) {
        String token = extractJwtToken(authorizationHeader);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        UserDTO user = jwtService.extractUser(token);

        return ResponseEntity.ok(taskService.removeWorker(taskID, email, user));
    }

    private String extractJwtToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
