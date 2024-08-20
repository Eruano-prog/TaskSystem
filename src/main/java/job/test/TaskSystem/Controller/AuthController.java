package job.test.TaskSystem.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import job.test.TaskSystem.Model.JwtTokenResponse;
import job.test.TaskSystem.Model.SignUpRequest;
import job.test.TaskSystem.Model.SignInRequest;
import job.test.TaskSystem.Service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для управления аутентификацией пользователей.
 * Предоставляет API для регистрации новых пользователей и аутентификации существующих пользователей.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "Контроллер для управления аутентификацией пользователей.Предоставляет API для регистрации новых пользователей и аутентификации существующих пользователей.")
public class AuthController {
    private final AuthenticationService authenticationService;

    /**
     * Регистрирует нового пользователя.
     *
     * @param request Информация о новом пользователе.
     * @return JWT токен пользователя.
     */
    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Эндпоинт для регистрации нового пользователя. Возвращает JWT токен в случае успешной регистрации."
    )
    @PostMapping("/signup")
    public ResponseEntity<JwtTokenResponse> signUp(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authenticationService.signUp(request));
    }

    /**
     * Аутентифицирует пользователя.
     *
     * @param request Информация для аутентификации пользователя.
     * @return JWT токен пользователя.
     */
    @Operation(
            summary = "Аутентификация пользователя",
            description = "Эндпоинт для аутентификации пользователя. Возвращает JWT токен в случае успешной аутентификации."
    )
    @PostMapping("/signin")
    public ResponseEntity<JwtTokenResponse> signIn(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(authenticationService.signIn(request));
    }
}
