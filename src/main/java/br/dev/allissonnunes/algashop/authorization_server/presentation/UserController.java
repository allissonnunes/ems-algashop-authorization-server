package br.dev.allissonnunes.algashop.authorization_server.presentation;

import br.dev.allissonnunes.algashop.authorization_server.application.user.management.AuthUserInput;
import br.dev.allissonnunes.algashop.authorization_server.application.user.management.AuthUserManagementApplicationService;
import br.dev.allissonnunes.algashop.authorization_server.application.user.management.AuthUserUpdateInput;
import br.dev.allissonnunes.algashop.authorization_server.application.user.query.AuthUserFilter;
import br.dev.allissonnunes.algashop.authorization_server.application.user.query.AuthUserOutput;
import br.dev.allissonnunes.algashop.authorization_server.application.user.query.AuthUserQueryService;
import br.dev.allissonnunes.algashop.authorization_server.application.user.query.PageModel;
import br.dev.allissonnunes.algashop.authorization_server.infrastructure.security.check.SecurityAnnotations;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
class UserController {

    private final AuthUserManagementApplicationService managementService;

    private final AuthUserQueryService authUserQueryService;

    @SecurityAnnotations.CanWriteUsers
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AuthUserOutput create(@RequestBody final @Valid AuthUserInput input) {
        return this.managementService.create(input);
    }

    @SecurityAnnotations.CanReadUsers
    @GetMapping
    public PageModel<AuthUserOutput> findAll(final AuthUserFilter filter) {
        return this.authUserQueryService.findAll(filter);
    }

    @SecurityAnnotations.CanReadUsers
    @GetMapping("/{userId}")
    public AuthUserOutput findById(@PathVariable final UUID userId) {
        return this.authUserQueryService.findById(userId);
    }

    @SecurityAnnotations.CanWriteUsers
    @PutMapping("/{userId}")
    public AuthUserOutput update(@PathVariable final UUID userId, @RequestBody final @Valid AuthUserUpdateInput input) {
        return this.managementService.update(userId, input);
    }

    @SecurityAnnotations.CanWriteUsers
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable final UUID userId) {
        this.managementService.delete(userId);
    }

}
