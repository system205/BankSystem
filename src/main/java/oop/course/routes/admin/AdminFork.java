package oop.course.routes.admin;


import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.routes.*;
import org.slf4j.*;

public class AdminFork implements Route {
    private static final Logger log = LoggerFactory.getLogger(AdminFork.class);

    private final Route[] routes;

    public AdminFork(Route... routes) {
        this.routes = routes;
    }

    @Override
    public Response act(Request request) throws Exception {
        String url = request.url().substring("/admin".length());
        for (Route r : routes) {
            if (r.accept(url)) {
                return r.act(request);
            }
        }
        log.debug("Suitable route is not found in /admin");
        return new NotFoundResponse();
    }

    @Override
    public boolean accept(String path) {
        return path.startsWith("/admin");
    }
}
