package job.test.TaskSystem.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на аутентицикацию пользователя")
public class SignInRequest {
    private String email;
    private String password;
}
