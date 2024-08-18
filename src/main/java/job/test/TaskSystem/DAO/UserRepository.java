package job.test.TaskSystem.DAO;

import job.test.TaskSystem.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByNickname(String nickname);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
