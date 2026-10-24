package br.dev.allissonnunes.algashop.authorization_server.infrastructure.security.check;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

public interface SecurityAnnotations {

    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @Documented
    @PreAuthorize("@oauth2.hasScope('users:read')")
    @interface CanReadUsers {

    }

    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @Documented
    @PreAuthorize("@oauth2.hasScope('users:write')")
    @interface CanWriteUsers {

    }

    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @Documented
    @PreAuthorize("@securityCheck.canAccessOwnProfile()")
    @interface CanAccessOwnProfile {

    }

}
