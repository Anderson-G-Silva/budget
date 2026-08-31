package dio.budget.auth.application;

import dio.budget.auth.application.input.PersistUserInput;
import dio.budget.auth.domain.User;
import dio.budget.auth.domain.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PersistUserUseCase {
    private final PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    public PersistUserUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User execute(PersistUserInput input) {
        var user = new User(input.username(), passwordEncoder.encode(input.password()), input.email(), input.role());
        return userRepository.save(user);
    }
}
