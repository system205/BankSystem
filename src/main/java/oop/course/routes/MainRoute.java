package oop.course.routes;

import oop.course.requests.Request;
import oop.course.responses.EmptyResponse;
import oop.course.responses.Response;
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
