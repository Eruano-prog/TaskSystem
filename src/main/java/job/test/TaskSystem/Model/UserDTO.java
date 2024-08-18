package job.test.TaskSystem.Model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    public Long id;
    public String nickName;
    public String email;
    public String password;
    public Role role;

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
