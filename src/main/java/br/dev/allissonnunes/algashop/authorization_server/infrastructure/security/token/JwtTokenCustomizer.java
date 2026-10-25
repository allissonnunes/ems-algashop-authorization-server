package br.dev.allissonnunes.algashop.authorization_server.infrastructure.security.token;

import br.dev.allissonnunes.algashop.authorization_server.infrastructure.security.oidc.OidcUserInfoService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Objects;

import static br.dev.allissonnunes.algashop.authorization_server.infrastructure.security.oidc.OidcUserInfoService.USER_CLAIM_TYPE;
import static java.util.Objects.requireNonNull;

@RequiredArgsConstructor
class JwtTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    public static final String USER_CLAIM_ROLE = "role";

    private final OidcUserInfoService oidcUserInfoService;

    @Override
    public void customize(final @NonNull JwtEncodingContext context) {
        if (isAccessToken(context) && isUserFlow(context)) {
            customizeAccessToken(context);
        } else if (isOidcIdToken(context)) {
            customizeOidcIdToken(context);
        }
    }

    private boolean isAccessToken(final @NonNull JwtEncodingContext context) {
        return OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType());
    }

    private boolean isUserFlow(final @NonNull JwtEncodingContext context) {
        return isUsingAuthorizationCode(context) || isUsingRefreshToken(context);
    }

    private boolean isUsingAuthorizationCode(final @NonNull JwtEncodingContext context) {
        return AuthorizationGrantType.AUTHORIZATION_CODE.equals(context.getAuthorizationGrantType());
    }

    private boolean isUsingRefreshToken(final @NonNull JwtEncodingContext context) {
        return AuthorizationGrantType.REFRESH_TOKEN.equals(context.getAuthorizationGrantType());
    }

    private boolean isOidcIdToken(final @NonNull JwtEncodingContext context) {
        return OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue());
    }

    private void customizeAccessToken(final @NonNull JwtEncodingContext context) {
        final OidcUserInfo oidcUserInfo = this.loadUser(context);
        context.getClaims().subject(requireNonNull(oidcUserInfo.getSubject()));
        context.getClaims().claim(USER_CLAIM_ROLE, requireNonNull(oidcUserInfo.getClaimAsString(USER_CLAIM_TYPE)));
    }

    private void customizeOidcIdToken(final @NonNull JwtEncodingContext context) {
        final OidcUserInfo oidcUserInfo = this.loadUser(context);
        context.getClaims().claims(claims -> claims.putAll(oidcUserInfo.getClaims()));
    }

    private @NonNull OidcUserInfo loadUser(final @NonNull JwtEncodingContext context) {
        return this.oidcUserInfoService.getOidcUserInfo(Objects.<Authentication>requireNonNull(context.getPrincipal()).getName());
    }

}
