package oop.course.auth;

import oop.course.interfaces.SecurityConfiguration;

public class AuthSecurityConfiguration implements SecurityConfiguration {

    public boolean isAccessibleUrl(String url) {
        // Check whether the user is allowed to access the specified URL
        return url.endsWith("/login") || url.endsWith("/register");
    }

    @Override
    public boolean isValidToken(String token) {
        // TODO - token validation
        return false;
    }
}
