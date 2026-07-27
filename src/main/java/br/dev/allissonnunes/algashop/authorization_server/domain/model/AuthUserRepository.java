package br.dev.allissonnunes.algashop.authorization_server.domain.model;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository extends CrudRepository<AuthUser, UUID> {

    Optional<AuthUser> findByEmail(String email);

}