package br.dev.allissonnunes.algashop.authorization_server.infrastructure.security;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "algashop.security")
@Validated
@Data
@NoArgsConstructor
public class AlgaShopSecurityProperties {

    @NotNull
    @Valid
    private CorsProperties cors;

    @NotNull
    @Valid
    private CspProperties csp;

    @NotNull
    @Valid
    private CookieProperties cookie;

    @Data
    @NoArgsConstructor
    public static class CorsProperties {

        @NotEmpty
        private List<String> allowedOrigins = new ArrayList<>();

    }

    @Data
    @NoArgsConstructor
    public static class CspProperties {

        @NotBlank
        private String policyDirectives;

    }

    @Data
    @NoArgsConstructor
    public static class CookieProperties {

        @NotEmpty
        private String sameSite;

        @NotBlank
        private String domainName;

        @NotNull
        private Boolean secure;

    }

}
