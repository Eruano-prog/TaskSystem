package job.test.TaskSystem.Controller;

import job.test.TaskSystem.Model.TaskDTO;
import job.test.TaskSystem.Service.JwtService;
import job.test.TaskSystem.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDTO>> tasks(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extractJwtToken(authorizationHeader);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        String username = jwtService.extractUserName(token);

        return ResponseEntity.ok(taskService.getAllAuthorTasks(username));
    }

    private String extractJwtToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
