package job.test.TaskSystem.Model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Пользователь")
public class UserDTO {
    public Long id;
    public String nickName;
    public String email;
    public String password;
    public String role;

    public User toEntity (){
        return new User(
                id,
                nickName,
                email,
                password,
                role
        );
    }
}
