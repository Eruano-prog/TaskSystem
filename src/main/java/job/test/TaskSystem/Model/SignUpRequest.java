package job.test.TaskSystem.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Запрос на идентификацию пользователя")
public class SignUpRequest {
    private String email;
    private String username;
    private String password;
}
