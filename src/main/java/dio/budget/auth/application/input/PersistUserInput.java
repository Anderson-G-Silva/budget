package dio.budget.auth.application.input;

import dio.budget.auth.domain.UserRole;

public record PersistUserInput(String username,
                               String password,
                               String email,
                               UserRole role) {
}
