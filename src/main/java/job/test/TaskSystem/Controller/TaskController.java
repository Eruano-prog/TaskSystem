package job.test.TaskSystem.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import job.test.TaskSystem.Model.TaskDTO;
import job.test.TaskSystem.Model.TaskPriority;
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

/**
 * Контроллер для управления задачами.
 * Предоставляет API для взаимодействия с задачами, включая получение, добавление, редактирование и удаление задач.
 */
@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@Tag(name = "Task Controller", description = "Controller provides API to interact with tasks")
public class TaskController {
    private final TaskService taskService;
    private final JwtService jwtService;

    /**
     * Получает все задачи текущего пользователя.
     *
     * @param authorizationHeader Заголовок авторизации с JWT токеном.
     * @param pageable            Параметры пагинации.
     * @return Страница DTO задач текущего пользователя.
     */
    @Operation(summary = "Получение всех задач текущего пользователя")
    @GetMapping()
    public ResponseEntity<Page<TaskDTO>> getTasksOfCurrentUser(@RequestHeader("Authorization") String authorizationHeader, Pageable pageable) {
        String token = extractJwtToken(authorizationHeader);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Page.empty());

        UserDTO user = jwtService.extractUser(token);

        return ResponseEntity.ok(taskService.getAllAuthorTasks(user, pageable));
    }

    /**
     * Получает все задачи пользователя по email.
     *
     * @param email    Email пользователя.
     * @param pageable Параметры пагинации.
     * @return Страница DTO задач пользователя.
     */
    @Operation(summary = "Получение всех задач пользователя по email")
    @GetMapping("/{email}")
    public ResponseEntity<Page<TaskDTO>> getTasksByEmails(@PathVariable String email, Pageable pageable) {
        return ResponseEntity.ok(taskService.getAllUserTasks(email, pageable));
    }

    /**
     * Получает все задачи пользователя по email с определённым статусом.
     *
     * @param email    Email пользователя.
     * @param status   Статус задачи.
     * @param pageable Параметры пагинации.
     * @return Страница DTO задач пользователя с определённым статусом.
     */
    @Operation(summary = "Получение всех задач пользователя по email с определённым статусом")
    @GetMapping("/{email}/status")
    public ResponseEntity<Page<TaskDTO>> getTasksByEmailAndStatus(@PathVariable String email, @RequestParam TaskStatus status, Pageable pageable) {
        return ResponseEntity.ok(taskService.getAllUserTasksByStatus(email, status, pageable));
    }

    /**
     * Получает все задачи пользователя по email с определённым приоритетом.
     *
     * @param email    Email пользователя.
     * @param priority Приоритет задачи.
     * @param pageable Параметры пагинации.
     * @return Страница DTO задач пользователя с определённым приоритетом.
     */
    @Operation(summary = "Получение всех задач пользователя по email с определённым приоритетом")
    @GetMapping("/{email}/priority")
    public ResponseEntity<Page<TaskDTO>> getTasksByEmailAndPriority(@PathVariable String email, @RequestParam TaskPriority priority, Pageable pageable) {
        return ResponseEntity.ok(taskService.getAllUserTasksByPriority(email, priority, pageable));
    }

    /**
     * Добавляет задачу текущему пользователю.
     *
     * @param authorizationHeader Заголовок авторизации с JWT токеном.
     * @param title               Заголовок задачи.
     * @param comment             Комментарий к задаче.
     * @param priority            Приоритет задачи.
     * @return DTO новой задачи.
     */
    @Operation(summary = "Добавить задачу текущему пользователю")
    @PostMapping()
    public ResponseEntity<TaskDTO> addTask(@RequestHeader("Authorization") String authorizationHeader, @RequestParam String title, @RequestParam String comment, @RequestParam(name = "priority", required = false, defaultValue = "Low") TaskPriority priority) {
        String token = extractJwtToken(authorizationHeader);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        UserDTO user = jwtService.extractUser(token);

        return ResponseEntity.ok(taskService.addTask(user, title, comment, priority));
    }

    /**
     * Изменяет заголовок или комментарий к задаче по её ID.
     *
     * @param authorizationHeader Заголовок авторизации с JWT токеном.
     * @param taskID              ID задачи.
     * @param title               Новый заголовок задачи.
     * @param comment             Новый комментарий к задаче.
     * @return Обновленный DTO задачи.
     */
    @Operation(summary = "Изменить заголовок или комментарий к задаче по её ID")
    @PutMapping()
    public ResponseEntity<TaskDTO> editTask(@RequestHeader("Authorization") String authorizationHeader, @RequestParam Long taskID, @RequestParam String title, @RequestParam String comment) {
        String token = extractJwtToken(authorizationHeader);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        UserDTO user = jwtService.extractUser(token);

        return ResponseEntity.ok(taskService.editTask(user, taskID, title, comment));
    }

    /**
     * Удаляет задачу по её ID.
     *
     * @param authorizationHeader Заголовок авторизации с JWT токеном.
     * @param taskID              ID задачи.
     * @return Сообщение об успешном удалении задачи.
     */
    @Operation(summary = "Удалить задачу по её ID")
    @DeleteMapping()
    public ResponseEntity<String> deleteTask(@RequestHeader("Authorization") String authorizationHeader, @RequestParam Long taskID) {
        String token = extractJwtToken(authorizationHeader);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        UserDTO user = jwtService.extractUser(token);

        taskService.deleteTask(user, taskID);
        return ResponseEntity.ok("Task deleted");
    }

    /**
     * Изменяет статус задачи по её ID.
     *
     * @param authorizationHeader Заголовок авторизации с JWT токеном.
     * @param taskID              ID задачи.
     * @param status              Новый статус задачи.
     * @return Обновленный DTO задачи.
     */
    @Operation(summary = "Изменить статус задачи по её ID")
    @PutMapping("/status")
    public ResponseEntity<TaskDTO> updateTaskStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestParam Long taskID, @RequestParam TaskStatus status) {
        String token = extractJwtToken(authorizationHeader);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        UserDTO user = jwtService.extractUser(token);

        return ResponseEntity.ok(taskService.changeStatus(taskID, status, user));
    }

    /**
     * Добавляет исполнителя к задаче по её ID.
     *
     * @param authorizationHeader Заголовок авторизации с JWT токеном.
     * @param email               Email исполнителя.
     * @param taskID              ID задачи.
     * @return Обновленный DTO задачи.
     */
    @Operation(summary = "Добавить исполнителя к задаче по её ID")
    @PutMapping("/worker")
    public ResponseEntity<TaskDTO> addWorker(@RequestHeader("Authorization") String authorizationHeader, @RequestParam String email, @RequestParam Long taskID) {
        String token = extractJwtToken(authorizationHeader);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        UserDTO user = jwtService.extractUser(token);

        return ResponseEntity.ok(taskService.addWorker(taskID, email, user));
    }

    /**
     * Удаляет исполнителя из задачи по её ID.
     *
     * @param authorizationHeader Заголовок авторизации с JWT токеном.
     * @param email               Email исполнителя.
     * @param taskID              ID задачи.
     * @return Обновленный DTO задачи.
     */
    @Operation(summary = "Удалить исполнителя из задачи по её ID")
    @DeleteMapping("/worker")
    public ResponseEntity<TaskDTO> removeWorker(@RequestHeader("Authorization") String authorizationHeader, @RequestParam String email, @RequestParam Long taskID) {
        String token = extractJwtToken(authorizationHeader);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        UserDTO user = jwtService.extractUser(token);

        return ResponseEntity.ok(taskService.removeWorker(taskID, email, user));
    }

    /**
     * Извлекает JWT токен из заголовка авторизации.
     *
     * @param authorizationHeader Заголовок авторизации.
     * @return JWT токен или null, если токен не найден.
     */
    private String extractJwtToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
