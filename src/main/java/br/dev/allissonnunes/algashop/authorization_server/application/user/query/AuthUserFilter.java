package br.dev.allissonnunes.algashop.authorization_server.application.user.query;

import br.dev.allissonnunes.algashop.authorization_server.domain.model.user.AuthUserType;
import org.springframework.data.domain.Sort;

public record AuthUserFilter(
        String email,
        AuthUserType type,
        int page,
        int size,
        String sort,
        Sort.Direction direction
) {

}
