package dio.budget.auth.infrastructure.security;

import dio.budget.auth.infrastructure.persistence.entity.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthenticatedUserProvider {

    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new IllegalStateException("Nenhum usuário autenticado no contexto.");
        }
        return ((UserEntity) authentication.getPrincipal()).getId();
    }
}
