package br.dev.allissonnunes.algashop.authorization_server.domain.model.user;

public class AuthUserEmailAlreadyInUseException extends RuntimeException {

    public AuthUserEmailAlreadyInUseException(final String email) {
        super("Email already in use: " + email, null, true, false);
    }

}
