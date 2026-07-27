package br.dev.allissonnunes.algashop.authorization_server.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

@Configuration
@RequiredArgsConstructor
class OAuth2TokenConfiguration {

    private final OidcUserInfoService oidcUserInfoService;

    @Bean
    OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())
                    && (AuthorizationGrantType.AUTHORIZATION_CODE.equals(context.getAuthorizationGrantType())
                    || AuthorizationGrantType.REFRESH_TOKEN.equals(context.getAuthorizationGrantType()))
            ) {
                final OidcUserInfo oidcUserInfo = this.oidcUserInfoService.getOidcUserInfo(context.getPrincipal().getName());
                context.getClaims()
                        .subject(oidcUserInfo.getSubject())
                        .claim(StandardClaimNames.PREFERRED_USERNAME, oidcUserInfo.getPreferredUsername());
            } else if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
                final OidcUserInfo oidcUserInfo = this.oidcUserInfoService.getOidcUserInfo(context.getPrincipal().getName());
                context.getClaims()
                        .claims(claims ->
                                claims.putAll(oidcUserInfo.getClaims()));
            }
        };
    }

}
