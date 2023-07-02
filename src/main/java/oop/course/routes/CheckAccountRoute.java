package oop.course.routes;

import oop.course.interfaces.*;
import oop.course.interfaces.Process;

public class CheckAccountRoute implements Route {

    private final Process next;

    public CheckAccountRoute(Process next) {
        this.next = next;
    }


    @Override
    public Response act(Request request) {
        return next.act(request);
    }

    @Override
    public boolean accept(String path) {
        return "/accounts".equals(path);
    }
}
