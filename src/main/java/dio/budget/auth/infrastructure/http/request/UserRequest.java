package dio.budget.auth.infrastructure.http.request;

import dio.budget.auth.application.input.PersistUserInput;
import dio.budget.auth.domain.UserRole;

public record UserRequest(String username,
                          String password,
                          String email,
                          UserRole role) {
    public PersistUserInput toInput() {
        return new PersistUserInput(username, password, email, role);
    }
}
