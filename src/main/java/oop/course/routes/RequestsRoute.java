package oop.course.routes;

import oop.course.exceptions.MalformedDataException;
import oop.course.interfaces.*;
import oop.course.responses.MethodNotAllowedResponse;
import org.slf4j.*;

public class RequestsRoute implements Route {
    private static final Logger log = LoggerFactory.getLogger(RequestsRoute.class);
    private final ProcessMethod[] processes;

    public RequestsRoute(ProcessMethod... processMethods) {
        this.processes = processMethods;
        log.trace("Created request route");
    }

    @Override
    public Response act(Request request) throws Exception {
        String method = request.method();
        for (ProcessMethod m : processes) {
            if (m.accept(method)) {
                return m.act(request);
            }
        }
        return new MethodNotAllowedResponse();
    }

    @Override
    public boolean accept(String path) {
        return "/requests".equals(path);
    }
}
