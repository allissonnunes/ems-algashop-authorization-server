package br.dev.allissonnunes.algashop.authorization_server.presentation;

import br.dev.allissonnunes.algashop.authorization_server.application.security.SecurityCheckApplicationService;
import br.dev.allissonnunes.algashop.authorization_server.application.user.query.AuthUserOutput;
import br.dev.allissonnunes.algashop.authorization_server.application.user.query.AuthUserQueryService;
import br.dev.allissonnunes.algashop.authorization_server.infrastructure.security.check.SecurityAnnotations;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/me")
@RequiredArgsConstructor
class UserMeController {

    private final SecurityCheckApplicationService securityCheck;

    private final AuthUserQueryService authUserQueryService;

    @SecurityAnnotations.CanAccessOwnProfile
    @GetMapping
    public ResponseEntity<AuthUserOutput> getMe() {
        return ResponseEntity.ok(authUserQueryService.findById(securityCheck.getAuthenticatedUserId()));
    }

}
