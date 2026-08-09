package br.dev.allissonnunes.algashop.authorization_server.application.user.query;

import br.dev.allissonnunes.algashop.authorization_server.domain.model.user.AuthUser;
import br.dev.allissonnunes.algashop.authorization_server.domain.model.user.AuthUserType;
import lombok.Builder;

import java.util.UUID;

@Builder
public record AuthUserOutput(
        UUID id,
        String name,
        String email,
        AuthUserType type,
        boolean enabled
) {

    public static AuthUserOutput from(final AuthUser user) {
        return AuthUserOutput.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .type(user.getType())
                .enabled(user.isEnabled())
                .build();
    }

}
