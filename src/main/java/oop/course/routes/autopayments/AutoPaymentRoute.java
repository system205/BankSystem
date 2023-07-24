package oop.course.routes.autopayments;

import oop.course.errors.exceptions.*;
import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.routes.*;

public class AutoPaymentRoute implements Route {
    private final ProcessMethod[] processes;

    public AutoPaymentRoute(ProcessMethod... methods) {
        this.processes = methods;
    }

    @Override
    public Response act(Request request) throws Exception {
        final String method = request.method();
        for (ProcessMethod process : processes) {
            if (process.accept(method)) {
                return process.act(request);
            }
        }
        throw new MethodNotAllowedException("Method not supported in /autopayments");
    }

    @Override
    public boolean accept(String path) {
        return "/autopayments".equals(path);
    }
}
