package br.dev.allissonnunes.algashop.authorization_server.infrastructure.security.oidc;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class OidcUserInfoMapper implements Function<OidcUserInfoAuthenticationContext, OidcUserInfo> {

    @Override
    public OidcUserInfo apply(final OidcUserInfoAuthenticationContext context) {
        final OAuth2Authorization authorization = context.getAuthorization();
        final OAuth2Authorization.Token<OidcIdToken> idTokenHolder = authorization.getToken(OidcIdToken.class);
        if (idTokenHolder == null) {
            final Authentication authentication = context.getAuthentication();
            if (authentication.getPrincipal() == null || !(authentication.getPrincipal() instanceof JwtAuthenticationToken jwtAuthenticationToken)) {
                throw new IllegalArgumentException();
            }
            return OidcUserInfo.builder()
                    .subject(jwtAuthenticationToken.getToken().getSubject())
                    .build();
        }
        return new OidcUserInfo(idTokenHolder.getClaims());
    }

}
