package job.test.TaskSystem.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Ответ с Jwt токеном")
public class JwtTokenResponse {
    private String token;
}
