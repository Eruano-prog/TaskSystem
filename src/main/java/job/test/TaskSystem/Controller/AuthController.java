package job.test.TaskSystem.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import job.test.TaskSystem.Model.JwtTokenResponse;
import job.test.TaskSystem.Model.SignUpRequest;
import job.test.TaskSystem.Model.SignInRequest;
import job.test.TaskSystem.Service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name="Authentication Controller", description="Controller present API to singUp or singIn")
public class AuthController {
    private final AuthenticationService authenticationService;


    @Operation(summary = "Идентификация нового пользователя")
    @PostMapping()
    public ResponseEntity<JwtTokenResponse> signUp(@RequestBody SignUpRequest request){
        return ResponseEntity.ok(authenticationService.signUp(request));
    }

    @Operation(summary = "Аутентификация пользователя ")
    @GetMapping()
    public ResponseEntity<JwtTokenResponse> signIn(@RequestBody SignInRequest request){
        return ResponseEntity.ok(authenticationService.signIn(request));
    }
}
