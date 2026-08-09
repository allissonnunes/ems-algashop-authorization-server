package br.dev.allissonnunes.algashop.authorization_server.infrastructure.security.check;

import br.dev.allissonnunes.algashop.authorization_server.application.security.SecurityCheckApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class OAuth2SecurityCheckApplicationServiceImpl implements SecurityCheckApplicationService {

    @Override
    public UUID getAuthenticatedUserId() {
        if (isMachineAuthentication()) {
            throw new AccessDeniedException("Machine authentication is not supported");
        }

        final Jwt jwt = getJwt();
        try {
            return UUID.fromString(jwt.getSubject());
        } catch (final IllegalArgumentException e) {
            log.error("Invalid user ID in JWT: {}", jwt.getSubject(), e);
            throw new AccessDeniedException("Invalid user ID", e);
        }
    }

    @Override
    public boolean isAuthenticated() {
        try {
            return getAuthentication().isAuthenticated();
        } catch (final IllegalStateException e) {
            log.warn("Failed to check authentication status", e);
            return false;
        }
    }

    @Override
    public boolean isMachineAuthentication() {
        final Jwt jwt;
        try {
            jwt = getJwt();
        } catch (final IllegalStateException e) {
            log.warn("Failed to get JWT", e);
            return false;
        }
        return jwt.getAudience().contains(jwt.getSubject());
    }

    @Override
    public boolean canAccessOwnProfile() {
        return this.isAuthenticated() && !this.isMachineAuthentication();
    }

    private Authentication getAuthentication() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("No authentication found in the context");
        }
        return authentication;
    }

    private Jwt getJwt() {
        final Authentication authentication = getAuthentication();
        if (!(authentication instanceof JwtAuthenticationToken jwtAuthenticationToken)) {
            throw new IllegalStateException("Authentication is not a JWT");
        }
        return jwtAuthenticationToken.getToken();
    }

}
