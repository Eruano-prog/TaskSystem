package job.test.TaskSystem.Service;

import jakarta.persistence.EntityExistsException;
import job.test.TaskSystem.Model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public JwtTokenResponse signUp(SignUpRequest request){
        if (userService.existUserByEmail(request.getEmail())){
            throw new EntityExistsException("");
        }

        User user = User.builder()
                .nickname(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.User)
                .build();

        userService.save(user);

        String jwt = jwtService.generateToken(user);

        return new JwtTokenResponse(jwt);
    }

    public JwtTokenResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        User user = userService.getUserByEmail(request.getEmail());

        var jwt = jwtService.generateToken(user);
        return new JwtTokenResponse(jwt);
    }


}
