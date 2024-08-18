package job.test.TaskSystem.DAO;

import job.test.TaskSystem.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    public List<Task> findAllByAuthorEmail(String email);
}
