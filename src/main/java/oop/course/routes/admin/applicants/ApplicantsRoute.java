package oop.course.routes.admin.applicants;

import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.routes.*;
import org.slf4j.*;

public class ApplicantsRoute implements Route {
    private static final Logger log = LoggerFactory.getLogger(ApplicantsRoute.class);

    private final ProcessMethod[] processes;

    public ApplicantsRoute(ProcessMethod... methods) {
        this.processes = methods;
    }

    @Override
    public Response act(Request request) throws Exception {
        String method = request.method();
        for (ProcessMethod m : processes) {
            if (m.accept(method)) {
                return m.act(request);
            }
        }
        log.debug("Method {} is not allowed in /offers", method);
        return new MethodNotAllowedResponse();
    }

    @Override
    public boolean accept(String path) {
        return "/offers".equals(path);
    }
}
