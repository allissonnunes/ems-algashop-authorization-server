package br.dev.allissonnunes.algashop.authorization_server.infrastructure.security.oidc;

import br.dev.allissonnunes.algashop.authorization_server.domain.model.user.AuthUser;
import br.dev.allissonnunes.algashop.authorization_server.domain.model.user.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OidcUserInfoService {

    private final AuthUserRepository authUserRepository;

    public @NonNull OidcUserInfo getOidcUserInfo(final @NonNull String username) {
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
