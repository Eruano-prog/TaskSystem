package job.test.TaskSystem.Controller;

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
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping()
    public ResponseEntity<JwtTokenResponse> signUp(@RequestBody SignUpRequest request){
        return ResponseEntity.ok(authenticationService.signUp(request));
    }

    @GetMapping()
    public ResponseEntity<JwtTokenResponse> signIn(@RequestBody SignInRequest request){
        return ResponseEntity.ok(authenticationService.signIn(request));
    }
}
