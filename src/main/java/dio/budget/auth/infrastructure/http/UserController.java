package dio.budget.auth.infrastructure.http;

import dio.budget.auth.application.PersistUserUseCase;
import dio.budget.auth.domain.User;
import dio.budget.auth.infrastructure.http.request.LoginRequest;
import dio.budget.auth.infrastructure.http.request.UserRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
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
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();

    public UserController(PersistUserUseCase persistUserUseCase,
                          AuthenticationManager authenticationManager) {
        this.persistUserUseCase = persistUserUseCase;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody UserRequest request) {
        persistUserUseCase.execute(request.toInput());

    }

    @PostMapping(value = "/api/auth/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Autentica o usuário")
    public ResponseEntity<Void> login(@RequestBody LoginRequest body,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(body.username(), body.password()));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/auth/me")
    public String hello(@AuthenticationPrincipal User user) {
        return "Bem vindo ao app de controle de orçamento - " + user.getId();
    }
}
