package oop.course.routes;

import oop.course.interfaces.*;

public class TransactionsRoute implements Route {

    private final ProcessMethod[] processes;

    public TransactionsRoute(ProcessMethod... methods) {
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
        throw new RuntimeException("Method not supported in /transactions");
    }

    @Override
    public boolean accept(String path) {
        return "/transactions".equals(path);
    }
}
