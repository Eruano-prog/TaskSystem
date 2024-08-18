package job.test.TaskSystem.Model;


import lombok.*;

@Getter
@Setter
@Builder
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
