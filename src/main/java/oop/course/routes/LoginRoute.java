package oop.course.routes;

import oop.course.interfaces.Process;
import oop.course.interfaces.*;

public class LoginRoute implements Route {
    private final Process next;

    public LoginRoute(Process next) {
        this.next = next;
    }

    @Override
    public Response act(Request request) {
        return next.act(request);


    }

    @Override
    public boolean accept(String path) {
        return "/login".equals(path);
    }

}
