package oop.course.routes;


import oop.course.errors.exceptions.NotFoundException;
import oop.course.requests.Request;
import oop.course.responses.Response;
import org.slf4j.*;

public class ManagerFork implements Route {
    private static final Logger log = LoggerFactory.getLogger(ManagerFork.class);
    private final Route[] routes;

    public ManagerFork(Route... routes) {
        this.routes = routes;
        log.debug("Created ManagerFork");
    }

    @Override
    public Response act(Request request) throws Exception {
        String url = request.url().substring("/manager".length());
        log.debug("Forking from manager with url: {}", url);
        for (Route r : routes) {
            if (r.accept(url)) {
                return r.act(request);
            }
        }
        throw new NotFoundException("No such route in /manager");
    }

    @Override
    public boolean accept(String path) {
        return path.startsWith("/manager");
    }
}
