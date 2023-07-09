package oop.course.routes;

import oop.course.interfaces.*;

public class AutoPaymentRoute implements Route {
    private final ProcessMethod[] processes;

    public AutoPaymentRoute(ProcessMethod... methods) {
        this.processes = methods;
    }

    @Override
    public Response act(Request request) {
        final String method = request.method();
        for (ProcessMethod process : processes) {
            if (process.accept(method)) {
                return process.act(request);
            }
        }
        throw new RuntimeException("Unsupported exception");
    }

    @Override
    public boolean accept(String path) {
        return "/autopayments".equals(path);
    }
}
