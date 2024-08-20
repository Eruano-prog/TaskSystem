package job.test.TaskSystem.DAO;

import job.test.TaskSystem.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторий для работы с пользователями.
 * Предоставляет методы для выполнения стандартных CRUD операций и специфических запросов.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
