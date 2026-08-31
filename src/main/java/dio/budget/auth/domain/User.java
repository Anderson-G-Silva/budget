package dio.budget.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private UserId id;
    private String username;
    private String password;
    private String email;
    private UserRole role;
    private Instant createdAt;

    public User(String username, String password, String email, UserRole role) {
        this.id = new UserId();
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
}
