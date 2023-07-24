package oop.course.server;

import oop.course.entity.url.URL;
import oop.course.errors.exceptions.NotFoundException;
import oop.course.routes.Process;
import oop.course.requests.Request;
import oop.course.responses.Response;
import oop.course.routes.*;

public class Fork implements Process {

    private final Route[] routes;
    private final URL url;

    public Fork(URL url, Route... routes) {
        this.url = url;
        this.routes = routes;
    }


    @Override
    public Response act(Request request) throws Exception {
        final String path = this.url.path(request);
        for (Route route : routes) {
            if (route.accept(path))
                return route.act(request);
        }
        throw new NotFoundException();
    }
}
