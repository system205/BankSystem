package oop.course.routes;

import oop.course.exceptions.MethodNotAllowedException;
import oop.course.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        log.error("Method " + method + " is not allowed in JobRoute");
        throw new MethodNotAllowedException("Method not supported in /job");
    }

    @Override
    public boolean accept(String path) {
        return "/job".equals(path);
    }
}
