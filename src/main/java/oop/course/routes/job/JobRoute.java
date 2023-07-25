package oop.course.routes.job;

import oop.course.errors.exceptions.*;
import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.routes.*;
import org.slf4j.*;

public class JobRoute implements Route {
    private final ProcessMethod[] processes;
    private static final Logger log = LoggerFactory.getLogger(JobRoute.class);

    public JobRoute(ProcessMethod... methods) {
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
        log.error("Method {} is not allowed in JobRoute", method);
        throw new MethodNotAllowedException("Method not supported in /job");
    }

    @Override
    public boolean accept(String path) {
        return "/job".equals(path);
    }
}
