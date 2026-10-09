package connected_vehicle.domain.auth;

import connected_vehicle.domain.auth.dto.LoginRequest;
import connected_vehicle.domain.auth.dto.LoginResponse;
import connected_vehicle.domain.user.User;
import connected_vehicle.domain.user.UserRepository;
import connected_vehicle.exception.InvalidCredentialsException;
import connected_vehicle.global.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public LoginResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.email()).orElseThrow(
                () -> new InvalidCredentialsException("이메일 또는 비밀번호가 틀렸습니다. 다시 시도해 주세요.")
        );

        if(!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new InvalidCredentialsException("이메일 또는 비밀번호가 틀렸습니다. 다시 시도해 주세요.");
        }

        String accessToken = jwtProvider.generateToken(user);

        return new LoginResponse(accessToken);
    }
}
