package br.dev.allissonnunes.algashop.authorization_server.infrastructure.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcLogoutAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.oidc.web.authentication.OidcLogoutAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.util.Optional;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
class OidcLogoutAuthenticationSuccessHandlerConfiguration {

    public static final OAuth2TokenType OIDC_ID_TOKEN_TYPE = new OAuth2TokenType(OidcParameterNames.ID_TOKEN);

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    private final SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();

    @Bean
    OidcLogoutAuthenticationSuccessHandler oidcLogoutAuthenticationSuccessHandler() {
        final OidcLogoutAuthenticationSuccessHandler logoutSuccessHandler = new OidcLogoutAuthenticationSuccessHandler();
        logoutSuccessHandler.setLogoutHandler(this::performLogout);
        return logoutSuccessHandler;
    }

    private void performLogout(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final @Nullable Authentication authentication
    ) {
        if (!(authentication instanceof OidcLogoutAuthenticationToken oidcLogoutAuthentication)) {
            return;
        }

        // Check for active user session
        if (oidcLogoutAuthentication.isPrincipalAuthenticated()) {
            this.removeToken(oidcLogoutAuthentication);
            this.securityContextLogoutHandler.logout(
                    request,
                    response,
                    (Authentication) oidcLogoutAuthentication.getPrincipal()
            );
        }
    }

    private void removeToken(final OidcLogoutAuthenticationToken oidcLogoutAuthentication) {
        Optional.ofNullable(oidcLogoutAuthentication.getIdToken())
                .flatMap(retrieveOAuth2Authorization())
                .ifPresent(this.oAuth2AuthorizationService::remove);

    }

    private @NonNull Function<OidcIdToken, Optional<OAuth2Authorization>> retrieveOAuth2Authorization() {
        return oidcIdToken ->
                Optional.ofNullable(this.oAuth2AuthorizationService
                        .findByToken(oidcIdToken.getTokenValue(), OIDC_ID_TOKEN_TYPE));
    }

}
