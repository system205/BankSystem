package oop.course.routes;

import oop.course.implementations.*;
import oop.course.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainRoute implements Route {
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
