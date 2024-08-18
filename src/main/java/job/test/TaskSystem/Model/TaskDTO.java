package job.test.TaskSystem.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    public Long id;
    public UserDTO author;
    public List<UserDTO> worker;
    public String title;
    public TaskStatus status;
    public String comment;

    public Task toEntity(){
        return new Task(
                id,
                author.toEntity(),
                worker.stream()
                        .map(UserDTO::toEntity)
                        .toList(),
                title,
                status,
                comment
        );
    }
}