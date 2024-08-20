package job.test.TaskSystem;

import jakarta.persistence.EntityExistsException;
import job.test.TaskSystem.Model.*;
import job.test.TaskSystem.Service.AuthenticationService;
import job.test.TaskSystem.Service.JwtService;
import job.test.TaskSystem.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTests {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private SignUpRequest signUpRequest;
    private SignInRequest signInRequest;
    private User user;

    @BeforeEach
    public void setUp() {
        signUpRequest = new SignUpRequest("testUser", "test@example.com", "password");
        signInRequest = new SignInRequest("test@example.com", "password");
        user = User.builder()
                .nickname("testUser")
                .email("test@example.com")
                .password(passwordEncoder.encode("password"))
                .role("User")
                .build();
    }

    @Test
    public void testSignUp_UserAlreadyExists() {
        when(userService.existUserByEmail("testUser")).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> {
            authenticationService.signUp(signUpRequest);
        });
    }

    @Test
    public void testSignUp_Success() {
        when(userService.existUserByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        JwtTokenResponse response = authenticationService.signUp(signUpRequest);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        verify(userService, times(1)).save(any(User.class));
    }

    @Test
    public void testSignIn_AuthenticationFailed() {
        doThrow(new AuthenticationException("Invalid credentials") {
        }).when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThrows(AuthenticationException.class, () -> {
            authenticationService.signIn(signInRequest);
        });
    }

    @Test
    public void testSignIn_Success() {
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        JwtTokenResponse response = authenticationService.signIn(signInRequest);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
    }
}
