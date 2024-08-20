package job.test.TaskSystem.Service;

import job.test.TaskSystem.DAO.UserRepository;
import job.test.TaskSystem.Model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Сервис для управления пользователями.
 * Предоставляет методы для получения пользователей по email, проверки существования пользователей,
 * загрузки пользователей по имени пользователя и сохранения пользователей.
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Получает пользователя по email.
     *
     * @param email Email пользователя.
     * @return Пользователь с указанным email.
     * @throws UsernameNotFoundException Если пользователь с указанным email не найден.
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    /**
     * Проверяет существование пользователя с указанным email.
     *
     * @param email Email пользователя.
     * @return {@code true}, если пользователь с указанным email существует, иначе {@code false}.
     */
    public Boolean existUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Загружает пользователя по имени пользователя.
     *
     * @param username Имя пользователя.
     * @return Пользователь с указанным именем пользователя.
     * @throws UsernameNotFoundException Если пользователь с указанным именем пользователя не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByEmail(username);
    }

    /**
     * Сохраняет пользователя.
     *
     * @param user Пользователь для сохранения.
     */
    public void save(User user) {
        userRepository.save(user);
    }
}
