package oop.course.routes.login;

import oop.course.entity.*;
import oop.course.errors.exceptions.*;
import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.routes.Process;
import oop.course.routes.*;
import oop.course.miscellaneous.implementations.*;
import oop.course.miscellaneous.interfaces.*;
import org.slf4j.*;

import java.sql.*;

public class LoginRoute implements Route {
    private final Process next;
    private final Connection connection;
    private final Logger log = LoggerFactory.getLogger(LoginRoute.class);

    public LoginRoute(Connection connection, Process next) {
        this.connection = connection;
        this.next = next;
    }

    @Override
    public Response act(Request request) throws Exception {
        Form form = new JsonForm(request.body());
        Customer customer = new Customer(connection, form);

        if (!customer.exists()) {
            log.error("Customer not found");
            throw new AuthorizationException("/login", "No such customer");
        }
        if (!customer.correctCredentials(form)) {
            throw new AuthorizationException("/login", "Wrong password");
        }
        return next.act(request);
    }

    @Override
    public boolean accept(String path) {
        return "/login".equals(path);
    }

}
