package oop.course.auth;

import oop.course.interfaces.SecurityConfiguration;

public class AuthSecurityConfiguration implements SecurityConfiguration {

    public boolean check(String url) {
        // Check whether the user is allowed to access the specified URL
        // TODO - I think we should check the access token.
        return url.endsWith("/login") || url.endsWith("/register");
    }
}
