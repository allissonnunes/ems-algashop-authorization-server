package br.dev.allissonnunes.algashop.authorization_server.domain.model.user;

import br.dev.allissonnunes.algashop.authorization_server.domain.model.DomainException;
import br.dev.allissonnunes.algashop.authorization_server.domain.model.IdGenerator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "auth_user")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthUser {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private AuthUserType type;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @CreatedBy
    @Column(name = "created_by")
    private UUID createdBy;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private UUID lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_at")
    private Instant lastModifiedAt;

    public static AuthUser brandNew(
            final String name,
            final String email,
            final AuthUserType type,
            final String passwordHash
    ) {
        final AuthUser user = new AuthUser();
        user.setId(IdGenerator.generateTimeBasedUUID());
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        user.setName(name);
        user.setType(type);
        user.setEnabled(true);
        return user;
    }

    private void setId(final UUID id) {
        requireNonNull(id, "id cannot be null");
        this.id = id;
    }

    private void setEmail(final String email) {
        requireNonNull(email, "email cannot be null");
        if (email.isBlank()) {
            throw new IllegalArgumentException("email cannot be blank");
        }
        this.email = email;
    }

    public void setPasswordHash(final String passwordHash) {
        requireNonNull(passwordHash, "passwordHash cannot be null");
        if (passwordHash.isBlank()) {
            throw new IllegalArgumentException("passwordHash cannot be blank");
        }
        this.passwordHash = passwordHash;
    }

    public void setName(final String name) {
        requireNonNull(name, "name cannot be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("name cannot be blank");
        }
        this.name = name;
    }

    public void setType(final AuthUserType type) {
        requireNonNull(type, "type cannot be null");
        if (AuthUserType.CUSTOMER == this.type) {
            throw new DomainException("Cannot change type of a CUSTOMER user");
        }
        this.type = type;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public void anonymize() {
        this.setName("Anonymized User");
        this.setEmail("anonymized-" + this.id + "@deleted.local");
        this.setEnabled(false);
    }

}