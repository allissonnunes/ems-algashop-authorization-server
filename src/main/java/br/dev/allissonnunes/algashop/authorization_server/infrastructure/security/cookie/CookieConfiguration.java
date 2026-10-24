package br.dev.allissonnunes.algashop.authorization_server.infrastructure.security.cookie;

import br.dev.allissonnunes.algashop.authorization_server.infrastructure.security.AlgaShopSecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
class CookieConfiguration {

    @Bean
    CookieSerializer cookieSerializer(final AlgaShopSecurityProperties properties) {
        final DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();

        cookieSerializer.setCookieName("JSESSIONID");
        cookieSerializer.setCookiePath("/");
        cookieSerializer.setUseHttpOnlyCookie(true);

        final var cookieProperties = properties.getCookie();
        cookieSerializer.setUseSecureCookie(cookieProperties.getSecure());
        cookieSerializer.setSameSite(cookieProperties.getSameSite());
        cookieSerializer.setDomainName(cookieProperties.getDomainName());

        return cookieSerializer;
    }

}
