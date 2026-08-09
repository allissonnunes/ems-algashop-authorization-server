package br.dev.allissonnunes.algashop.authorization_server.domain.model.user;

import java.util.UUID;

public class AuthUserNotFoundException extends RuntimeException {

    public AuthUserNotFoundException(final UUID userId) {
        super("User not found with ID: " + userId, null, true, false);
    }

}
