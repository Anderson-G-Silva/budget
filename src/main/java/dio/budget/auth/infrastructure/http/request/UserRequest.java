package dio.budget.auth.infrastructure.http.request;

import dio.budget.auth.application.input.PersistUserInput;
import dio.budget.auth.domain.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequest(
        @NotBlank
        String username,
        @NotNull
        String password,
        @NotNull
        @Email
        String email,
        @NotNull
        UserRole role) {
    public PersistUserInput toInput() {
        return new PersistUserInput(username, password, email, role);
    }
}
