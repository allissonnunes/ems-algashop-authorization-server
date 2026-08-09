package br.dev.allissonnunes.algashop.authorization_server.application.user.management;

import br.dev.allissonnunes.algashop.authorization_server.domain.model.user.AuthUserType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthUserUpdateInput(
        @NotBlank
        String name,
        @NotNull
        AuthUserType type,
        @NotNull
        Boolean enabled
) {

}
