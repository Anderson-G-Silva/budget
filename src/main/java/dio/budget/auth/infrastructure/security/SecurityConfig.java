package dio.budget.auth.infrastructure.security;

import dio.budget.auth.domain.User;
import dio.budget.auth.domain.UserRepository;
import dio.budget.auth.infrastructure.persistence.repository.UserEntityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.UUID;

import static dio.budget.auth.domain.UserRole.ROLE_ADMIN;
import static dio.budget.auth.domain.UserRole.ROLE_USER;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class SecurityConfig {
    // Configurações do ponto de entrada
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, RestUsernamePasswordAuthenticationFilter restUsernamePasswordAuthenticationFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .securityContext(context -> context.requireExplicitSave(false))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/api/auth/login").permitAll()
                        .requestMatchers("/admin/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated())

                .addFilterAt(restUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuditorAware<UUID> auditorProvider(AuthenticatedUserProvider userProvider) {
        return new AuditorAwareImpl(userProvider);
    }

    @Bean
    CommandLineRunner initDatabase(UserRepository repository, UserEntityRepository entityRepository) {
        return args -> {

            if (entityRepository.count() == 0) {

                var passord = (passwordEncoder().encode("Admin"));
                var Admin = new User("Admin",passord,"Admin@gmail.com", ROLE_ADMIN);
                repository.save(Admin);


                passord = (passwordEncoder().encode("password_A"));
                var userA = new User("UserA",passord,"userA@gmail.com", ROLE_USER);
                repository.save(userA);

                passord = (passwordEncoder().encode("password_B"));
                var userB = new User("UserB",passord,"userB@gmail.com", ROLE_USER);
                repository.save(userB);

                passord = (passwordEncoder().encode("password_C"));
                var userC = new User("UserC",passord,"userC@gmail.com", ROLE_USER);
                repository.save(userC);

            }
        };
    }
}