package oop.course.routes;

import oop.course.interfaces.*;

public class JobRoute implements Route {
    private final ProcessMethod[] processes;

    public JobRoute(ProcessMethod... methods) {
        this.processes = methods;
    }

    @Override
    public Response act(Request request) {
        String method = request.method();
        for (ProcessMethod m : processes) {
            if (m.accept(method)) {
                return m.act(request);
            }
        }
        throw new RuntimeException("Method " + method + " is not allowed in JobRoute");
    }

    @Override
    public boolean accept(String path) {
        return "/job".equals(path);
    }
}