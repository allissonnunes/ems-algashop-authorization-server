package br.dev.allissonnunes.algashop.authorization_server.infrastructure.persistence;

import br.dev.allissonnunes.algashop.authorization_server.application.security.SecurityCheckApplicationService;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware", dateTimeProviderRef = "dateTimeProvider")
public class SpringDataJpaAuditingConfiguration {

    @Bean
    AuditorAware<@NonNull UUID> auditorAware(final SecurityCheckApplicationService securityCheck) {
        return () -> {
            if (!securityCheck.isAuthenticated() || securityCheck.isMachineAuthentication()) {
                return Optional.empty();
            }
            return Optional.of(securityCheck.getAuthenticatedUserId());
        };
    }

    @Bean
    DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(Instant.now());
    }

}
