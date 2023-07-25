package oop.course.routes.transactions;

import oop.course.errors.exceptions.*;
import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.routes.*;

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
        throw new MethodNotAllowedException("Method not supported in /transactions");
    }

    @Override
    public boolean accept(String path) {
        return "/transactions".equals(path);
    }
}
