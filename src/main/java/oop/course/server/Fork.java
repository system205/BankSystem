package oop.course.server;

import oop.course.interfaces.Process;
import oop.course.interfaces.*;
import oop.course.responses.*;
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
        return new NotFoundResponse();
    }
}
