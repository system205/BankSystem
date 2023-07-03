package oop.course.routes;

import oop.course.interfaces.*;

public class CheckAccountRoute implements Route {

    private final ProcessMethod[] next;

    public CheckAccountRoute(ProcessMethod... processMethods) {
        this.next = processMethods;
    }


    @Override
    public Response act(Request request) {
        final String method = request.method();
        for (ProcessMethod process : next) {
            if (process.accept(method)) {
                return process.act(request);
            }
        }
        throw new RuntimeException("Unsupported exception");
    }

    @Override
    public boolean accept(String path) {
        return "/accounts".equals(path);
    }
}
