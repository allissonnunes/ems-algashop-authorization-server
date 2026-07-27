package br.dev.allissonnunes.algashop.authorization_server.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Set;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class AuthorizationServerSecurityConfiguration {

    private final OidcUserInfoMapper oidcUserInfoMapper;

    @Bean
    @Order(1)
    SecurityFilterChain authorizationServerSecurityFilterChain(final HttpSecurity http) {
        http.oauth2AuthorizationServer(authorizationServer -> {
            http.securityMatcher(authorizationServer.getEndpointsMatcher());
            authorizationServer.oidc(oidcConfigurer -> oidcConfigurer
                    .userInfoEndpoint(userInfoEndpointCustomizer ->
                            userInfoEndpointCustomizer.userInfoMapper(this.oidcUserInfoMapper)));
        });
        http.authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated());
        http.oauth2ResourceServer((resourceServer) -> resourceServer.jwt(withDefaults()));
        http.exceptionHandling((exceptions) ->
                exceptions.defaultAuthenticationEntryPointFor(
                        new LoginUrlAuthenticationEntryPoint("/login"),
                        this.createRequestMatcher()
                )
        );
        return http.build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain defaultSecurityFilterChain(final HttpSecurity http) {
        http.authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated());
        http.formLogin(withDefaults());
        return http.build();
    }

    private RequestMatcher createRequestMatcher() {
        MediaTypeRequestMatcher requestMatcher = new MediaTypeRequestMatcher(MediaType.TEXT_HTML);
        requestMatcher.setIgnoredMediaTypes(Set.of(MediaType.ALL));
        return requestMatcher;
    }

}
