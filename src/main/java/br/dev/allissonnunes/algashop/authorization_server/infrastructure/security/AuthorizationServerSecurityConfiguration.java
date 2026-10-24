package br.dev.allissonnunes.algashop.authorization_server.infrastructure.security;

import br.dev.allissonnunes.algashop.authorization_server.infrastructure.security.oidc.OidcUserInfoMapper;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.health.actuate.endpoint.HealthEndpoint;
import org.springframework.boot.security.autoconfigure.actuate.web.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.authorization.DefaultOAuth2AuthorizationManagerFactory;
import org.springframework.security.oauth2.core.authorization.OAuth2AuthorizationManagerFactory;
import org.springframework.security.oauth2.server.authorization.oidc.web.authentication.OidcLogoutAuthenticationSuccessHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

import java.util.Set;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableJdbcHttpSession
@RequiredArgsConstructor
class AuthorizationServerSecurityConfiguration {

    private final OidcUserInfoMapper oidcUserInfoMapper;

    private final OidcLogoutAuthenticationSuccessHandler oidcLogoutAuthenticationSuccessHandler;

    private final AlgaShopSecurityProperties properties;

    @Bean
    @Order(1)
    SecurityFilterChain authorizationServerSecurityFilterChain(final HttpSecurity http) {
        http.oauth2AuthorizationServer(authorizationServer -> {
            http.securityMatcher(authorizationServer.getEndpointsMatcher());
            authorizationServer.oidc(oidcConfigurer -> oidcConfigurer
                    .userInfoEndpoint(userInfoEndpointCustomizer -> userInfoEndpointCustomizer
                            .userInfoMapper(this.oidcUserInfoMapper))
                    .logoutEndpoint(logoutEndpointCustomizer -> logoutEndpointCustomizer
                            .logoutResponseHandler(oidcLogoutAuthenticationSuccessHandler))
            );
        });
        http.authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated());
        http.cors(withDefaults());
        http.exceptionHandling((exceptions) ->
                exceptions.defaultAuthenticationEntryPointFor(
                        new LoginUrlAuthenticationEntryPoint("/login"),
                        this.createRequestMatcher()
                )
        );
        http.headers(headers -> {
            headers.xssProtection(withDefaults());
            headers.contentSecurityPolicy(cspPolicy -> cspPolicy
                    .policyDirectives(this.properties.getCsp().getPolicyDirectives()));
        });
        http.oauth2ResourceServer((resourceServer) -> resourceServer.jwt(withDefaults()));
        return http.build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain apiSecurityFilterChain(final HttpSecurity http) {
        http.securityMatcher("/api/**");
        http.authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated());
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(withDefaults());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(withDefaults()));
        return http.build();
    }

    @Bean
    @Order(3)
    SecurityFilterChain defaultSecurityFilterChain(final HttpSecurity http) {
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(EndpointRequest.to(HealthEndpoint.class)).permitAll()
                .anyRequest().authenticated()
        );
        http.formLogin(withDefaults());
        return http.build();
    }

    @Bean
    OAuth2AuthorizationManagerFactory<?> oauth2() {
        return new DefaultOAuth2AuthorizationManagerFactory<>();
    }

    private @NonNull RequestMatcher createRequestMatcher() {
        MediaTypeRequestMatcher requestMatcher = new MediaTypeRequestMatcher(MediaType.TEXT_HTML);
        requestMatcher.setIgnoredMediaTypes(Set.of(MediaType.ALL));
        return requestMatcher;
    }

}
