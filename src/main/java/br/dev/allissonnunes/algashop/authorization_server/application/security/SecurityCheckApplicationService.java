package br.dev.allissonnunes.algashop.authorization_server.application.security;

import java.util.UUID;

public interface SecurityCheckApplicationService {

    UUID getAuthenticatedUserId();

    boolean isAuthenticated();

    boolean isMachineAuthentication();

    boolean canAccessOwnProfile();

}
