package br.dev.allissonnunes.algashop.authorization_server.domain.model.user;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository extends CrudRepository<AuthUser, UUID>, JpaSpecificationExecutor<AuthUser> {

    Optional<AuthUser> findByEmail(String email);

    boolean existsByEmail(String email);

}