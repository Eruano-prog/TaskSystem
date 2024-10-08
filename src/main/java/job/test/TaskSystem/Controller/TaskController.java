package job.test.TaskSystem.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для управления задачами.
 * Предоставляет API для взаимодействия с задачами, включая получение, добавление, редактирование и удаление задач.
 */
@Validated
@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@Tag(name = "Task Controller", description = "Контроллер для управления задачами. Предоставляет API для взаимодействия с задачами, включая получение, добавление, редактирование и удаление задач.")
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
    @Operation(
            summary = "Получение всех задач текущего пользователя",
            description = "Эндпоинт для получения всех задач текущего пользователя. Возвращает страницу DTO задач текущего пользователя."
    )
    @GetMapping()
    public ResponseEntity<Page<TaskDTO>> getTasksOfCurrentUser(
            @RequestHeader("Authorization") @Parameter(description = "Заголовок авторизации с JWT токеном") String authorizationHeader,
            Pageable pageable)
    {
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
    @Operation(
            summary = "Получение всех задач пользователя по email",
            description = "Эндпоинт для получения всех задач пользователя по email. Возвращает страницу DTO задач пользователя."
    )
    @GetMapping("/{email}")
    public ResponseEntity<Page<TaskDTO>> getTasksByAuthorEmails(
            @PathVariable @Email(message = "Invalid email format") @Size(max = 255, message = "email can`t be more than 255 long") @Parameter(description = "Email пользователя") String email,
            Pageable pageable) {
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
    @Operation(
            summary = "Получение всех задач пользователя по email с определённым статусом",
            description = "Эндпоинт для получения всех задач пользователя по email с определённым статусом. Возвращает страницу DTO задач пользователя с определённым статусом."
    )
    @GetMapping("/{email}/status")
    public ResponseEntity<Page<TaskDTO>> getTasksByAuthorEmailAndStatus(
            @PathVariable @Email(message = "Invalid email format") @Size(max = 255, message = "email can`t be more than 255 long") @Parameter(description = "Email пользователя") String email,
            @RequestParam @Parameter(description = "Статус задачи") TaskStatus status,
            Pageable pageable)
    {
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
    @Operation(
            summary = "Получение всех задач пользователя по email с определённым приоритетом",
            description = "Эндпоинт для получения всех задач пользователя по email с определённым приоритетом. Возвращает страницу DTO задач пользователя с определённым приоритетом."
    )
    @GetMapping("/{email}/priority")
    public ResponseEntity<Page<TaskDTO>> getTasksByAuthorEmailAndPriority(
            @PathVariable @Email(message = "Invalid email format") @Size(max = 255, message = "email can`t be more than 255 long") @Parameter(description = "Email пользователя") String email,
            @RequestParam @Parameter(description = "Приоритет задачи") TaskPriority priority,
            Pageable pageable)
    {
        return ResponseEntity.ok(taskService.getAllUserTasksByPriority(email, priority, pageable));
    }

    /**
     * Получает все задачи исполнителя по email.
     *
     * @param email    Email пользователя.
     * @param pageable Параметры пагинации.
     * @return Страница DTO задач пользователя.
     */
    @Operation(
            summary = "Получение всех задач исполнителя по email",
            description = "Эндпоинт для получения всех задач исполнителя по email. Возвращает страницу DTO задач пользователя."
    )
    @GetMapping("/worker/{email}")
    public ResponseEntity<Page<TaskDTO>> getTasksByWorkerEmail(
            @PathVariable @Email(message = "Invalid email format") @Size(max = 255, message = "email can`t be more than 255 long") @Parameter(description = "Email пользователя") String email,
            Pageable pageable) {
        return ResponseEntity.ok(taskService.getAllWorkerTasks(email, pageable));
    }

    /**
     * Получает все задачи исполнителя по email с определённым статусом.
     *
     * @param email    Email пользователя.
     * @param status   Статус задачи.
     * @param pageable Параметры пагинации.
     * @return Страница DTO задач пользователя с определённым статусом.
     */
    @Operation(
            summary = "Получение всех задач исполнителя по email с определённым статусом",
            description = "Эндпоинт для получения всех задач исполнителя по email с определённым статусом. Возвращает страницу DTO задач пользователя с определённым статусом."
    )
    @GetMapping("/worker/{email}/status")
    public ResponseEntity<Page<TaskDTO>> getTasksByWorkerEmailAndStatus(
            @PathVariable @Email(message = "Invalid email format") @Size(max = 255, message = "email can`t be more than 255 long") @Parameter(description = "Email пользователя") String email,
            @RequestParam @Parameter(description = "Статус задачи") TaskStatus status,
            Pageable pageable)
    {
        return ResponseEntity.ok(taskService.getAllWorkerTasksByStatus(email, status, pageable));
    }

    /**
     * Получает все задачи исполнителя по email с определённым приоритетом.
     *
     * @param email    Email пользователя.
     * @param priority Приоритет задачи.
     * @param pageable Параметры пагинации.
     * @return Страница DTO задач пользователя с определённым приоритетом.
     */
    @Operation(
            summary = "Получение всех задач исполнителя по email с определённым приоритетом",
            description = "Эндпоинт для получения всех задач исполнителя по email с определённым приоритетом. Возвращает страницу DTO задач пользователя с определённым приоритетом."
    )
    @GetMapping("/worker/{email}/priority")
    public ResponseEntity<Page<TaskDTO>> getTasksByWorkerEmailAndPriority(
            @PathVariable @Email(message = "Invalid email format") @Size(max = 255, message = "email can`t be more than 255 long") @Parameter(description = "Email пользователя") String email,
            @RequestParam @Parameter(description = "Приоритет задачи") TaskPriority priority,
            Pageable pageable)
    {
        return ResponseEntity.ok(taskService.getAllWorkerTasksByPriority(email, priority, pageable));
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
    @Operation(
            summary = "Добавить задачу текущему пользователю",
            description = "Эндпоинт для добавления задачи текущему пользователю. Возвращает DTO новой задачи."
    )
    @PostMapping()
    public ResponseEntity<TaskDTO> addTask(
            @RequestHeader("Authorization") @Parameter(description = "Заголовок авторизации с JWT токеном") String authorizationHeader,
            @RequestParam @NotEmpty(message = "Title is required") @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters") @Parameter(description = "Заголовок задачи") @Schema(minLength = 1, maxLength = 255) String title,
            @RequestParam(name = "comment", required = false, defaultValue = "") @Size(max = 1000, message = "Description must be between 0 and 1000 characters") @Parameter(description = "Комментарий к задаче") @Schema(maxLength = 1000) String comment,
            @RequestParam(name = "priority", required = false, defaultValue = "Low") @Parameter(description = "Приоритет задачи") TaskPriority priority)
    {
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
    @Operation(
            summary = "Изменить заголовок или комментарий к задаче по её ID",
            description = "Эндпоинт для изменения заголовка или комментария к задаче по её ID. Возвращает обновленный DTO задачи."
    )
    @PutMapping()
    public ResponseEntity<TaskDTO> editTask(
            @RequestHeader("Authorization") @Parameter(description = "Заголовок авторизации с JWT токеном") String authorizationHeader,
            @RequestParam @NotNull(message = "Task ID is required") @Min(1) @Parameter(description = "ID задачи") Long taskID,
            @RequestParam @NotEmpty(message = "Title is required") @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters") @Parameter(description = "Новый заголовок задачи") @Schema(minLength = 1, maxLength = 255) String title,
            @RequestParam(name = "comment", required = false, defaultValue = "") @Size(max = 1000, message = "Description must be between 0 and 1000 characters") @Parameter(description = "Новый комментарий к задаче") @Schema(maxLength = 1000) String comment)
    {
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
    @Operation(
            summary = "Удалить задачу по её ID",
            description = "Эндпоинт для удаления задачи по её ID. Возвращает сообщение об успешном удалении задачи."
    )
    @DeleteMapping()
    public ResponseEntity<String> deleteTask(
            @RequestHeader("Authorization") @Parameter(description = "Заголовок авторизации с JWT токеном") String authorizationHeader,
            @RequestParam @Min(1) @Parameter(description = "ID задачи") Long taskID)
    {
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
    @Operation(
            summary = "Изменить статус задачи по её ID",
            description = "Эндпоинт для изменения статуса задачи по её ID. Возвращает обновленный DTO задачи."
    )
    @PutMapping("/status")
    public ResponseEntity<TaskDTO> updateTaskStatus(
            @RequestHeader("Authorization") @Parameter(description = "Заголовок авторизации с JWT токеном") String authorizationHeader,
            @RequestParam @Min(1) @Parameter(description = "ID задачи") Long taskID,
            @RequestParam @Parameter(description = "Новый статус задачи") TaskStatus status)
    {
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
    @Operation(
            summary = "Добавить исполнителя к задаче по её ID",
            description = "Эндпоинт для добавления исполнителя к задаче по её ID. Возвращает обновленный DTO задачи."
    )
    @PutMapping("/worker")
    public ResponseEntity<TaskDTO> addWorker(
            @RequestHeader("Authorization") @Parameter(description = "Заголовок авторизации с JWT токеном") String authorizationHeader,
            @RequestParam @Email(message = "Invalid email format") @Size(max = 255, message = "email can`t be more than 255 long") @Parameter(description = "Email исполнителя") @Schema(maxLength = 255) String email,
            @RequestParam @Min(1) @Parameter(description = "ID задачи") Long taskID)
    {
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
    @Operation(
            summary = "Удалить исполнителя из задачи по её ID",
            description = "Эндпоинт для удаления исполнителя из задачи по её ID. Возвращает обновленный DTO задачи."
    )
    @DeleteMapping("/worker")
    public ResponseEntity<TaskDTO> removeWorker(
            @RequestHeader("Authorization") @Parameter(description = "Заголовок авторизации с JWT токеном") String authorizationHeader,
            @RequestParam @Email(message = "Invalid email format") @Size(max = 255, message = "email can`t be more than 255 long") @Parameter(description = "Email исполнителя") @Schema(maxLength = 255) String email,
            @RequestParam @Min(1) @Parameter(description = "ID задачи") Long taskID)
    {
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
