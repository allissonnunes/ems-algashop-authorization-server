package br.dev.allissonnunes.algashop.authorization_server.application.security;

import br.dev.allissonnunes.algashop.authorization_server.domain.model.user.AuthUserType;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;

import java.util.UUID;

public interface SecurityCheckApplicationService {

    UUID getAuthenticatedUserId();

    boolean isAuthenticated();

    boolean isMachineAuthentication();

    boolean canAccessOwnProfile();

    boolean canRegisterUserOfType(AuthUserType type, MethodSecurityExpressionOperations root);

}
