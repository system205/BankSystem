package oop.course.routes;

import oop.course.interfaces.*;

public class AdminFork implements Route {
    private final Route[] routes;

    public AdminFork(Route... routes) {
        this.routes = routes;
    }

    @Override
    public Response act(Request request) {
        String url = request.url().substring("/admin".length());
        for (Route r : routes) {
            if (r.accept(url)) {
                return r.act(request);
            }
        }
        throw new RuntimeException("Suitable route is not found in /admin");
    }

    @Override
    public boolean accept(String path) {
        return path.startsWith("/admin");
    }
}
