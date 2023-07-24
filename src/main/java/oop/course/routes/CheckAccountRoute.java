package oop.course.routes;

import oop.course.exceptions.MethodNotAllowedException;
import oop.course.interfaces.*;

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
