package oop.course.auth;

import oop.course.auth.interfaces.SecurityConfiguration;
import oop.course.exceptions.MalformedDataException;
import oop.course.implementations.*;
import oop.course.interfaces.*;
import oop.course.interfaces.Process;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Authorization implements Process {
    private static final Logger logger = LoggerFactory.getLogger(Authorization.class);

    private final Optional<Process> next;
    private final SecurityConfiguration securityConfiguration;

    public Authorization(Optional<Process> next, SecurityConfiguration securityConfiguration) {
        this.next = next;
        this.securityConfiguration = securityConfiguration;
    }

    public Authorization(SecurityConfiguration securityConfiguration) {
        this(Optional.empty(), securityConfiguration);
    }

    @Override
    public Response act(Request request) throws Exception {
        // Internal logic
        String url = new SimpleUrl().path(request);
        if (!securityConfiguration.isValidToken(request.headers(), url) && !securityConfiguration.isAccessibleUrl(url)) {
            logger.error("Authentication token is invalid");
            return new ForbiddenResponse("Authentication token is invalid");
        }
        // Process next if OK so far
        if (this.next.isPresent()) {
            logger.info("Authorization stage has passed successfully");
            return this.next.get().act(request);
        } else {
            // Unreachable state (exception is supposed to appear)
            logger.error("Authorization returned an empty response");
            return new EmptyResponse();
        }
    }
}
