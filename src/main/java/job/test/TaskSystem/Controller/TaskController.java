package job.test.TaskSystem.Controller;

import job.test.TaskSystem.Model.TaskDTO;
import job.test.TaskSystem.Model.TaskStatus;
import job.test.TaskSystem.Model.UserDTO;
import job.test.TaskSystem.Service.JwtService;
import job.test.TaskSystem.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    TaskService taskService;
    JwtService jwtService;

    @Autowired
    public TaskController(TaskService taskService, JwtService jwtService){
        this.taskService = taskService;
        this.jwtService = jwtService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskDTO>> tasks(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extractJwtToken(authorizationHeader);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        String username = jwtService.extractUserName(token);

        return ResponseEntity.ok(taskService.getAllAuthorTasks(username));
    }

    @PostMapping("/add")
    public ResponseEntity<TaskDTO> addTask(@RequestHeader("Authorization") String authorizationHeader, @RequestBody TaskDTO taskDTO) {
        String token = extractJwtToken(authorizationHeader);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        UserDTO user = jwtService.extractUser(token);

        return ResponseEntity.ok(taskService.addTask(user, taskDTO));
    }

    @PutMapping("/edit")
    public ResponseEntity<TaskDTO> editTask(@RequestHeader("Authorization") String authorizationHeader, @RequestBody TaskDTO taskDTO) {
        String token = extractJwtToken(authorizationHeader);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        UserDTO user = jwtService.extractUser(token);

        return ResponseEntity.ok(taskService.editTask(user, taskDTO));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteTask(@RequestHeader("Authorization") String authorizationHeader, @RequestParam Long taskID) {
        String token = extractJwtToken(authorizationHeader);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        UserDTO user = jwtService.extractUser(token);

        taskService.deleteTask(user, taskID);
        return ResponseEntity.ok("Task deleted");
    }

    @PutMapping("/status")
    public ResponseEntity<TaskDTO> updateTaskStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestParam Long taskID, @RequestParam TaskStatus status) {
        String token = extractJwtToken(authorizationHeader);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        UserDTO user = jwtService.extractUser(token);

        return ResponseEntity.ok(taskService.changeStatus(taskID, status, user));
    }

    @PutMapping("/worker/add")
    public ResponseEntity<TaskDTO> addWorker(@RequestHeader("Authorization") String authorizationHeader, @RequestParam String email, @RequestParam Long taskID) {
        String token = extractJwtToken(authorizationHeader);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        UserDTO user = jwtService.extractUser(token);

        return ResponseEntity.ok(taskService.addWorker(taskID, email, user));
    }

    @PutMapping("/worker/remove")
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
