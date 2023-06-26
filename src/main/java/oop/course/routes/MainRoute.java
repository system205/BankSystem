package oop.course.routes;

import oop.course.interfaces.*;

public class MainRoute implements Route {
    @Override
    public Response act(Request request) {
        return null;
    }

    @Override
    public boolean accept(String path) {
        return "/".equals(path);
    }
}
