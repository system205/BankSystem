package oop.course.auth;

import oop.course.auth.interfaces.SecurityConfiguration;
import oop.course.implementations.*;
import oop.course.interfaces.*;
import oop.course.interfaces.Process;

import java.util.*;

public class Authorization implements Process {

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
    public Response act(Request request) {
        // Internal logic
        String url = new SimpleUrl().path(request);
        if (!securityConfiguration.isValidToken(request.headers(), url)) {
            return new ForbiddenResponse("Authentication token is invalid");
        }
        // Process next if OK so far
        if (this.next.isPresent()) {
            return this.next.get().act(request);
        } else { // Unreachable state (exception is supposed to appear)
            return new EmptyResponse();
        }
    }
}
