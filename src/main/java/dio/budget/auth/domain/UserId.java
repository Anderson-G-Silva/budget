package dio.budget.auth.domain;

import java.util.UUID;

public record UserId(UUID id) {
    public UserId () {this(UUID.randomUUID());}
}
