package job.test.TaskSystem.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Задача")
public class TaskDTO {
    public Long id;
    public UserDTO author;
    public List<UserDTO> worker;
    public String title;
    public String status;
    public String comment;

    public Task toEntity(){
        return new Task(
                id,
                Optional.ofNullable(author).map(UserDTO::toEntity).orElse(null),
                Optional.ofNullable(worker)
                        .map(list -> list.stream()
                                .map(UserDTO::toEntity)
                                .toList())
                        .orElse(null),
                title,
                Optional.ofNullable(status).map(TaskStatus::valueOf).orElse(null),
                comment
        );
    }
}