package oop.course.routes;

import oop.course.entity.*;
import oop.course.exceptions.MalformedDataException;
import oop.course.implementations.*;
import oop.course.interfaces.*;
import oop.course.responses.*;
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
                        new HeaderToken(
                                request.headers()
                        ).id(),
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
