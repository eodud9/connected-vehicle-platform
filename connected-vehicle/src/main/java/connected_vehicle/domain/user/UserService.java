package connected_vehicle.domain.user;

import connected_vehicle.domain.user.dto.UserCreateRequest;
import connected_vehicle.domain.user.dto.UserCreateResponse;
import connected_vehicle.exception.DuplicateEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserCreateResponse createUser(UserCreateRequest request){

        if(userRepository.existsByEmail(request.email())){
            throw new DuplicateEmailException("입력하신 이메일이 존재합니다. 다른 이메일을 사용해 주세요.");
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        User user = new User(request.name(), request.email(), encodedPassword);

        User savedUser = userRepository.save(user);

        return new UserCreateResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getCreatedAt());
    }
}
