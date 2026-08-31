package dio.budget.auth.infrastructure.http;

import dio.budget.auth.application.PersistUserUseCase;
import dio.budget.auth.domain.User;
import dio.budget.auth.infrastructure.http.request.UserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final PersistUserUseCase persistUserUseCase;

    public UserController(PersistUserUseCase persistUserUseCase) {
        this.persistUserUseCase = persistUserUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody UserRequest request) {
        persistUserUseCase.execute(request.toInput());

    }

    @GetMapping("/api/auth/login")
    public String hello(@AuthenticationPrincipal User user) {
        return "Bem vindo ao app de controle de orçamento - " + user.getId();
    }
}
