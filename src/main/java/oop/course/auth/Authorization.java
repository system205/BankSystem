package oop.course.auth;

import oop.course.entity.url.*;
import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.routes.Process;
import org.slf4j.*;

/**
 * This class is responsible for checking whether a user with the given role can access the given url
 */
public final class Authorization implements Process {
    private static final Logger logger = LoggerFactory.getLogger(Authorization.class);
    private final Process next;
    private final GuardedUrl guardedUrl;

    public Authorization(Process next, GuardedUrl guardedUrl) {
        this.next = next;
        this.guardedUrl = guardedUrl;
    }

    @Override
    public Response act(Request request) throws Exception {
        // throws exception if user does not have a necessary role
        this.guardedUrl.path(request);
        // Process next if OK so far
        logger.info("Authorization stage has passed successfully");
        return this.next.act(request);
    }
}
