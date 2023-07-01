package oop.course.routes;

import oop.course.interfaces.*;
import oop.course.responses.*;

public class NotFoundRoute implements Route {

    @Override
    public Response act(Request request) {
        return new NotFoundResponse();
    }

    @Override
    public boolean accept(String path) {
        return true;
    }
}
