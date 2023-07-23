package oop.course.auth;

import oop.course.implementations.*;
import oop.course.interfaces.*;
import oop.course.interfaces.Process;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

/**
 * This class is responsible for checking whether a user with the given role can access the given url
 */
public class Authorization implements Process {
    private static final Logger logger = LoggerFactory.getLogger(Authorization.class);

    private final Process next;

    private final RolesConfiguration rolesConfiguration;
    private final Connection connection;


    public Authorization(Process next, Connection connection, RolesConfiguration rolesConfiguration) {
        this.next = next;
        this.connection = connection;
        this.rolesConfiguration = rolesConfiguration;
    }

    @Override
    public Response act(Request request) throws Exception {
        // throws exception if user does not have a necessary role
        // TODO - creating object inside another object - bad
        new GuardedUrl(connection, rolesConfiguration).path(request);
        // Process next if OK so far
        logger.info("Authorization stage has passed successfully");
        return this.next.act(request);
    }
}
