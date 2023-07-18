package oop.course.routes;

import oop.course.entity.*;

import oop.course.implementations.*;
import oop.course.interfaces.*;
import oop.course.responses.*;
import org.slf4j.*;

import java.sql.*;

public class AllAccounts implements Route {
    private static final Logger log = LoggerFactory.getLogger(AllAccounts.class);

    private final Connection connection;

    public AllAccounts(Connection connection) {
        log.info("New AllAccounts object");
        this.connection = connection;
    }

    @Override
    public Response act(Request request) throws Exception {
        log.debug("Start retrieving all accounts");
        return new SuccessResponse(
                new Customer(
                        this.connection,
                        new HeaderToken(request.headers()).id()
                ).accounts()
        );
    }

    @Override
    public boolean accept(String path) {
        log.trace("Accept of AllAccounts with path {}", path);
        return "/accounts".equals(path);
    }
}
