package oop.course.routes;

import oop.course.errors.exceptions.NotFoundException;
import oop.course.requests.Request;
import oop.course.responses.Response;

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
