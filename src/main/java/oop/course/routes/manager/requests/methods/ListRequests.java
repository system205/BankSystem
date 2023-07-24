package oop.course.routes.manager.requests.methods;

import oop.course.entity.*;
import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.routes.*;
import org.slf4j.*;

import java.sql.*;

public class ListRequests implements ProcessMethod {
    private static final Logger log = LoggerFactory.getLogger(ListRequests.class);
    private final Connection connection;

    public ListRequests(Connection connection) {
        this.connection = connection;
        log.trace("New ListRequests");
    }

    @Override
    public Response act(Request request) throws Exception {
        log.debug("Call manager to list requests");
        return new SuccessResponse(
                new Manager(
                        this.connection
                ).requests()
        );
    }

    @Override
    public boolean accept(String method) {
        log.trace("Check method {} in ListRequests", method);
        return "GET".equals(method);
    }
}
