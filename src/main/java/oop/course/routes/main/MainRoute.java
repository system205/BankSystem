package oop.course.routes.main;

import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.routes.*;
import org.slf4j.*;

public final class MainRoute implements Route {
    private final Logger log = LoggerFactory.getLogger(MainRoute.class);

    @Override
    public Response act(Request request) throws Exception {
        log.info("Start main route");
        return new EmptyResponse();
    }

    @Override
    public boolean accept(String path) {
        return "/".equals(path);
    }
}
