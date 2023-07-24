package oop.course.routes;

import oop.course.errors.exceptions.*;
import oop.course.requests.*;
import oop.course.responses.*;

public class Fork implements Process {
    private final Route[] routes;

    public Fork(Route... routes) {
        this.routes = routes;
    }


    @Override
    public Response act(Request request) throws Exception {
        final String path = request.url();
        for (Route route : routes) {
            if (route.accept(path))
                return route.act(request);
        }
        throw new NotFoundException();
    }
}
