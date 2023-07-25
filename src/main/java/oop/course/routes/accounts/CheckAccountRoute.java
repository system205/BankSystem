package oop.course.routes.accounts;

import oop.course.errors.exceptions.*;
import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.routes.*;

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
