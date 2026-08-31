package dio.budget.auth.infrastructure.security;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;
import java.util.UUID;

public class AuditorAwareImpl implements AuditorAware<UUID> {

    private final AuthenticatedUserProvider userProvider;
    public AuditorAwareImpl(AuthenticatedUserProvider userProvider) {
        this.userProvider = userProvider;
    }

    @Override
    public Optional<UUID> getCurrentAuditor() {
        try {
            return Optional.of(userProvider.getCurrentUserId());
        } catch (IllegalStateException e) {
            return Optional.empty();
        }
    }
}