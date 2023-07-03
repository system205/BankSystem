package oop.course.auth;

import oop.course.auth.interfaces.SecurityConfiguration;
import oop.course.entity.Customer;
import oop.course.implementations.HeaderToken;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;

public class AuthSecurityConfiguration implements SecurityConfiguration {
    private final Connection connection;
    private final RolesConfiguration rolesConfiguration;

    public AuthSecurityConfiguration(Connection connection, RolesConfiguration rolesConfiguration) {
        this.connection = connection;
        this.rolesConfiguration = rolesConfiguration;
    }

    public boolean isAccessibleUrl(String url) {
        // Check whether the user is allowed to access the specified URL
        return url.endsWith("/login") || url.endsWith("/register");
    }

    @Override
    public boolean isValidToken(Collection<String> headers, String url) {
        try {
            final String userId = new HeaderToken(headers).id();
            final List<String> roles = new Customer(connection, userId).getRoles();
            boolean isAllowed = false;
            for (String role : roles) {
                if (rolesConfiguration.isAllowedToGo(role, url)) {
                    isAllowed = true;
                    break;
                }
            }
            return isAllowed;
        } catch (Exception e) {
//            System.out.println(e.getMessage());
            return false;
        }
    }
}
