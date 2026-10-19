package br.dev.allissonnunes.algashop.authorization_server.infrastructure.security.oidc;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.oidc.web.authentication.OidcLogoutAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
class OidcLogoutAuthenticationSuccessHandlerConfiguration {

    @Bean
    OidcLogoutAuthenticationSuccessHandler oidcLogoutAuthenticationSuccessHandler(
            final LogoutHandler odicRevokeAuthorizationsLogoutHandler
    ) {
        final OidcLogoutAuthenticationSuccessHandler logoutSuccessHandler = new OidcLogoutAuthenticationSuccessHandler();
        logoutSuccessHandler.setLogoutHandler(odicRevokeAuthorizationsLogoutHandler);
        return logoutSuccessHandler;
    }

}
