package oop.course.routes;

import oop.course.implementations.*;
import oop.course.interfaces.*;

public class MainRoute implements Route {
    @Override
    public Response act(Request request) {
        System.out.println("Start main route");
        return new EmptyResponse();
    }

    @Override
    public boolean accept(String path) {
        return "/".equals(path);
    }
}
