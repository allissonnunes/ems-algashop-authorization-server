package br.dev.allissonnunes.algashop.authorization_server.infrastructure.security.oidc;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class OidcRevokeAuthorizationsService {

    private final JdbcClient jdbcClient;

    public int revokeAuthorizations(final @NonNull Authentication authentication) {
        final String principalName = authentication.getName();
        return jdbcClient.sql("DELETE FROM oauth2_authorization WHERE principal_name = ?")
                .param(1, principalName)
                .update();
    }

}
