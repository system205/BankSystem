package oop.course.routes;

import oop.course.exceptions.MalformedDataException;
import oop.course.interfaces.*;
import oop.course.responses.*;
import org.slf4j.*;

public class ManagerFork implements Route {
    private static final Logger log = LoggerFactory.getLogger(ManagerFork.class);
    private final Route[] routes;

    public ManagerFork(Route... routes) {
        this.routes = routes;
        log.debug("Created ManagerFork");
    }

    @Override
    public Response act(Request request) throws MalformedDataException {
        String url = request.url().substring("/manager".length());
        log.debug("Forking from manager with url: {}", url);
        for (Route r : routes) {
            if (r.accept(url)) {
                return r.act(request);
            }
        }
        return new NotFoundResponse();
    }

    @Override
    public boolean accept(String path) {
        return path.startsWith("/manager");
    }
}
