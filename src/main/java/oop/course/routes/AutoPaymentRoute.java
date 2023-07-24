package oop.course.routes;

import oop.course.errors.exceptions.MethodNotAllowedException;
import oop.course.requests.Request;
import oop.course.responses.Response;

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
