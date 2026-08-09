package br.dev.allissonnunes.algashop.authorization_server.infrastructure.security.query;

import br.dev.allissonnunes.algashop.authorization_server.domain.model.user.AuthUser;
import br.dev.allissonnunes.algashop.authorization_server.domain.model.user.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class AuthUserDetailsService implements UserDetailsService {

    private final AuthUserRepository authUserRepository;

    @Override
    public @NonNull UserDetails loadUserByUsername(final @NonNull String username) throws UsernameNotFoundException {
        return this.authUserRepository.findByEmail(username)
                .map(this::mapToUserDetails)
                .orElseThrow(() -> UsernameNotFoundException.fromUsername(username));
    }

    private UserDetails mapToUserDetails(final AuthUser authUser) {
        return User.withUsername(authUser.getEmail())
                .password(authUser.getPasswordHash())
                .disabled(!authUser.isEnabled())
                .build();
    }

}
