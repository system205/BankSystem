package oop.course.entity.url;

import oop.course.auth.RolesConfiguration;
import oop.course.entity.Customer;
import oop.course.errors.exceptions.ForbiddenException;
import oop.course.entity.HeaderToken;
import oop.course.requests.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Collection;

public class GuardedUrl implements URL {
    private static final Logger logger = LoggerFactory.getLogger(GuardedUrl.class);
    private final Connection connection;
    private final RolesConfiguration rolesConfiguration;

    public GuardedUrl(Connection connection, RolesConfiguration rolesConfiguration) {
        this.connection = connection;
        this.rolesConfiguration = rolesConfiguration;
    }

    @Override
    public String path(Request request) throws Exception {
        String url = request.url();
        if (rolesConfiguration.isAllowedToGo(url)) {
            return url;
        }

        final Collection<String> roles = new Customer(
                connection,
                new HeaderToken(request.headers()).id()
        ).roles();

        for (String role : roles) {
            if (this.rolesConfiguration.isAllowedToGo(url, role)) {
                return url;
            }
        }
        logger.debug("User with roles {} is not allowed to go to {}", roles, url);
        throw new ForbiddenException(String.format("User with roles %s is not allowed to go to %s", roles, url));
    }
}
