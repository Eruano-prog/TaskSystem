package job.test.TaskSystem.DAO;

import job.test.TaskSystem.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByAuthorEmail(String email);
    Optional<Task> findByIdAndAuthorEmail(Long id, String email);
    Boolean existsByTitleAndAuthorEmail(String title, String email);
}
