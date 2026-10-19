package br.dev.allissonnunes.algashop.authorization_server.infrastructure.security.oidc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcLogoutAuthenticationToken;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
class OdicRevokeAuthorizationsLogoutHandler implements LogoutHandler {

    private final SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();

    private final OidcRevokeAuthorizationsService oidcRevokeAuthorizationsService;

    @Override
    public void logout(
            final @NonNull HttpServletRequest request,
            final @NonNull HttpServletResponse response,
            final @Nullable Authentication authentication
    ) {
        this.performLogout(request, response, authentication);
        this.revokeAuthorizations(authentication);
    }

    private void performLogout(final @NonNull HttpServletRequest request,
                               final @NonNull HttpServletResponse response,
                               final @Nullable Authentication authentication) {
        if (!(authentication instanceof OidcLogoutAuthenticationToken oidcLogoutAuthentication)) {
            return;
        }

        // Check for active user session
        if (oidcLogoutAuthentication.isPrincipalAuthenticated()) {
            this.securityContextLogoutHandler.logout(request, response,
                    (Authentication) oidcLogoutAuthentication.getPrincipal());
        }
    }

    private void revokeAuthorizations(final @Nullable Authentication authentication) {
        if (authentication != null) {
            final int revokeCount = this.oidcRevokeAuthorizationsService.revokeAuthorizations(authentication);
            log.debug("Revoked {} authorizations for user {}", revokeCount, authentication.getName());
        }
    }

}
