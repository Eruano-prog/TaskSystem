package job.test.TaskSystem.Service;

import jakarta.persistence.EntityExistsException;
import job.test.TaskSystem.Model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Сервис для обеспечения идентификации и аутентификации пользователя.
 * Предоставляет методы для регистрации новых пользователей и аутентификации существующих пользователей.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Регистрирует нового пользователя и возвращает его JWT токен.
     *
     * @param request Информация о новом пользователе.
     * @return JWT токен пользователя.
     * @throws EntityExistsException Если пользователь с указанным email уже существует.
     */
    public JwtTokenResponse signUp(SignUpRequest request) {
        if (userService.existUserByEmail(request.getEmail())) {
            throw new EntityExistsException("User already exists");
        }

        User user = User.builder()
                .nickname(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("User")
                .build();

        userService.save(user);

        String jwt = jwtService.generateToken(user);

        return new JwtTokenResponse(jwt);
    }

    /**
     * Аутентифицирует пользователя и возвращает его JWT токен.
     *
     * @param request Информация для аутентификации пользователя.
     * @return JWT токен пользователя.
     * @throws org.springframework.security.core.AuthenticationException Если аутентификация не удалась.
     */
    public JwtTokenResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        User user = userService.getUserByEmail(request.getEmail());

        String jwt = jwtService.generateToken(user);
        return new JwtTokenResponse(jwt);
    }
}
