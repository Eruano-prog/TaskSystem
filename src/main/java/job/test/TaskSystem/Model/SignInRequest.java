package job.test.TaskSystem.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Запрос на аутентицикацию пользователя")
public class SignInRequest {
    @NotBlank(message = "Username is mandatory")
    @Size(max = 255)
    private String email;

    @NotBlank(message = "Username is mandatory")
    @Size(max = 255)
    private String password;
}
