package br.dev.allissonnunes.algashop.authorization_server.application.user.management;

import br.dev.allissonnunes.algashop.authorization_server.domain.model.user.AuthUserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AuthUserInput(
        @NotBlank
        String name,

        @NotBlank
        @Email
        String email,

        @NotNull
        AuthUserType type
) {

}
