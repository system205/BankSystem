package oop.course.routes.checkAccount;

import oop.course.errors.exceptions.MethodNotAllowedException;
import oop.course.requests.Request;
import oop.course.responses.Response;
import oop.course.routes.ProcessMethod;
import oop.course.routes.Route;

public class CheckAccountRoute implements Route {

    private final ProcessMethod[] next;

    public CheckAccountRoute(ProcessMethod... processMethods) {
        this.next = processMethods;
    }


    @Override
    public Response act(Request request) throws Exception {
        final String method = request.method();
        for (ProcessMethod process : next) {
            if (process.accept(method)) {
                return process.act(request);
            }
        }
        throw new MethodNotAllowedException("Method not supported in /account");
    }

    @Override
    public boolean accept(String path) {
        return "/account".equals(path);
    }
}
