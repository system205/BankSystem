package oop.course.routes.manager.requests;

import oop.course.errors.exceptions.*;
import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.routes.*;
import org.slf4j.*;

public class CustomerRequestsRoute implements Route {
    private static final Logger log = LoggerFactory.getLogger(CustomerRequestsRoute.class);
    private final ProcessMethod[] processes;

    public CustomerRequestsRoute(ProcessMethod... methods) {
        this.processes = methods;
        log.trace("Created new {}", CustomerRequestsRoute.class);
    }

    @Override
    public Response act(Request request) throws Exception {
        String method = request.method();
        log.debug("Request with method {} received", method);
        for (ProcessMethod m : processes) {
            if (m.accept(method)) {
                return m.act(request);
            }
        }
        throw new MethodNotAllowedException("Method not supported in /requests");
    }

    @Override
    public boolean accept(String path) {
        log.trace("Check path {}", path);
        return path.startsWith("/requests");
    }
}
