package br.dev.allissonnunes.algashop.authorization_server.infrastructure.security;

import br.dev.allissonnunes.algashop.authorization_server.domain.model.AuthUser;
import br.dev.allissonnunes.algashop.authorization_server.domain.model.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class OidcUserInfoService {

    private final AuthUserRepository authUserRepository;

    @NonNull OidcUserInfo getOidcUserInfo(final @NonNull String username) {
        final AuthUser loggedUser = this.authUserRepository.findByEmail(username)
                .orElseThrow(() -> UsernameNotFoundException.fromUsername(username));

        return OidcUserInfo.builder()
                .email(loggedUser.getEmail())
                .name(loggedUser.getName())
                .preferredUsername(loggedUser.getEmail())
                .subject(loggedUser.getId().toString())
                .updatedAt(loggedUser.getLastModifiedAt().toString())
                .build();
    }

}
