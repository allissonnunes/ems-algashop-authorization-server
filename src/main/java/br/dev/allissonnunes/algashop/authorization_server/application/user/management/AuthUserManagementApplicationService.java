package br.dev.allissonnunes.algashop.authorization_server.application.user.management;

import br.dev.allissonnunes.algashop.authorization_server.application.user.query.AuthUserOutput;
import br.dev.allissonnunes.algashop.authorization_server.domain.model.user.AuthUser;
import br.dev.allissonnunes.algashop.authorization_server.domain.model.user.AuthUserEmailAlreadyInUseException;
import br.dev.allissonnunes.algashop.authorization_server.domain.model.user.AuthUserNotFoundException;
import br.dev.allissonnunes.algashop.authorization_server.domain.model.user.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthUserManagementApplicationService {

    private final AuthUserRepository authUserRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthUserOutput create(final AuthUserInput input) {
        if (this.authUserRepository.existsByEmail(input.email())) {
            throw new AuthUserEmailAlreadyInUseException(input.email());
        }

        final String tempPassword = RandomStringUtils.secure().nextAlphabetic(12);
        final String passwordHash = this.passwordEncoder.encode(tempPassword);

        final AuthUser user = AuthUser.brandNew(
                input.name(),
                input.email(),
                input.type(),
                passwordHash
        );

        this.authUserRepository.save(user);

        return AuthUserOutput.from(user);
    }

    public AuthUserOutput update(final UUID userId, final AuthUserUpdateInput input) {
        final AuthUser user = authUserRepository.findById(userId)
                .orElseThrow(() -> new AuthUserNotFoundException(userId));

        user.setName(input.name());
        user.setType(input.type());
        user.setEnabled(input.enabled());

        return AuthUserOutput.from(authUserRepository.save(user));
    }

    public void delete(final UUID userId) {
        final AuthUser user = this.authUserRepository.findById(userId)
                .orElseThrow(() -> new AuthUserNotFoundException(userId));
        user.anonymize();
        this.authUserRepository.save(user);
    }

}
