package br.dev.allissonnunes.algashop.authorization_server.infrastructure.security.token;

import br.dev.allissonnunes.algashop.authorization_server.infrastructure.security.oidc.OidcUserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
class OAuth2TokenConfiguration {

    @Bean
    OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer(final OidcUserInfoService oidcUserInfoService) {
        return new JwtTokenCustomizer(oidcUserInfoService);
    }

}
