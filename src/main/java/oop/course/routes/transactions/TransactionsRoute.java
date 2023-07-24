package oop.course.routes.transactions;

import oop.course.errors.exceptions.MethodNotAllowedException;
import oop.course.requests.Request;
import oop.course.responses.Response;
import oop.course.routes.ProcessMethod;
import oop.course.routes.Route;

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
