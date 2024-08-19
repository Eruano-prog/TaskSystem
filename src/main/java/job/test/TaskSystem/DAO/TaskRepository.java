package job.test.TaskSystem.DAO;

import job.test.TaskSystem.Model.Task;
import job.test.TaskSystem.Model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findAllByAuthorEmail(String email, Pageable pageable);
    Page<Task> findAllByAuthorEmailAndStatus(String email, TaskStatus status, Pageable pageable);

    Optional<Task> findByIdAndAuthorEmail(Long id, String email);

    Boolean existsByTitleAndAuthorEmail(String title, String email);

}
