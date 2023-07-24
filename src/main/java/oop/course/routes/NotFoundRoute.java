package oop.course.routes;

import oop.course.exceptions.NotFoundException;
import oop.course.interfaces.*;

public class NotFoundRoute implements Route {

    @Override
    public Response act(Request request) throws Exception {
        throw new NotFoundException();
    }

    @Override
    public boolean accept(String path) {
        return true;
    }
}
