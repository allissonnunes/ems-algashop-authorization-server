package br.dev.allissonnunes.algashop.authorization_server.infrastructure.security.cors;

import br.dev.allissonnunes.algashop.authorization_server.infrastructure.security.AlgaShopSecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
class AlgaShopCorsConfiguration {

    @Bean
    CorsConfigurationSource corsConfigurationSource(final AlgaShopSecurityProperties properties) {
        final CorsConfiguration cors = new CorsConfiguration();

        cors.setAllowedOrigins(properties.getCors().getAllowedOrigins());
        cors.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        cors.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "Accept"));
        cors.setAllowCredentials(true);
        cors.applyPermitDefaultValues();

        final UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", cors);
        return corsSource;
    }

}
