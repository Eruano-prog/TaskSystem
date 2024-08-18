package job.test.TaskSystem.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue
    public Long id;

    @OneToOne
    public User author;

    @OneToMany
    public List<User> workers;

    public String title;
    public TaskStatus status;
    public String comment;

    public void loadFromDTO(TaskDTO dto){
        author = dto.author.toEntity();
        workers = dto.worker.stream()
                .map(UserDTO::toEntity)
                .toList();
        title = dto.title;
        status = dto.status;
        comment = dto.comment;
    }

    public TaskDTO toDTO(){
        return new TaskDTO(
                id,
                author.toDTO(),
                workers.stream()
                        .map(User::toDTO)
                        .toList(),
                title,
                status,
                comment
        );
    }

    public void addWorker(User worker){
        this.workers.add(worker);
    }

    public void removeWorker(User worker){
        this.workers.remove(worker);
    }
}
