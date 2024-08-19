package job.test.TaskSystem.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_id_seq")
    @SequenceGenerator(name = "task_id_seq", sequenceName = "task_id_seq", allocationSize = 1)
    public Long id;

    @ManyToOne
    public User author;

    @OneToMany(fetch = FetchType.EAGER)
    public List<User> workers;

    public String title;
    public TaskStatus status;
    public String comment;

    public void loadFromDTO(TaskDTO dto){
        Optional.ofNullable(dto.worker)
                .ifPresent(workers -> this.workers = workers.stream()
                        .map(UserDTO::toEntity)
                        .toList());

        Optional.ofNullable(dto.title).ifPresent(title -> this.title = title);
        Optional.ofNullable(dto.status).ifPresent(status -> this.status = TaskStatus.valueOf(status));
        Optional.ofNullable(dto.comment).ifPresent(comment -> this.comment = comment);
    }

    public TaskDTO toDTO(){
        return new TaskDTO(
                id,
                author.toDTO(),
                workers.stream()
                        .map(User::toDTO)
                        .toList(),
                title,
                status.name(),
                comment
        );
    }

    public void addWorker(User worker){
        this.workers.add(worker);
    }

    public void removeWorker(User worker){
        this.workers.removeIf(lambda -> lambda.getId().equals(worker.getId()));
    }
}
