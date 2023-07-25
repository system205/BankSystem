package oop.course.routes.notFound;

import oop.course.errors.exceptions.*;
import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.routes.*;

public final class NotFoundRoute implements Route {

    @Override
    public Response act(Request request) throws Exception {
        throw new NotFoundException();
    }

    @Override
    public boolean accept(String path) {
        return true;
    }
}
