package oop.course.auth;

import oop.course.auth.interfaces.SecurityConfiguration;
import oop.course.entity.Customer;
import oop.course.exceptions.AuthorizationException;
import oop.course.implementations.HeaderToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Collection;

public class AuthSecurityConfiguration implements SecurityConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(AuthSecurityConfiguration.class);
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
        final String userId;
        try {
            userId = new HeaderToken(headers).id();
        } catch (AuthorizationException e) {
            logger.error("Error appeared in HeaderToken class");
            return false;
        }
        final Collection<String> roles = new Customer(connection, userId).roles();
        for (String role : roles) {
            if (rolesConfiguration.isAllowedToGo(role, url)) {
                return true;
            }
        }
        logger.info("This user is not allowed to access the specified URL");
        return false;
    }
}
