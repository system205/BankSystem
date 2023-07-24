package oop.course.routes;

import oop.course.entity.*;
import oop.course.requests.Request;
import oop.course.responses.*;
import oop.course.tools.implementations.*;
import oop.course.tools.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class RegisterRoute implements Route {
    private final Logger log = LoggerFactory.getLogger(RegisterRoute.class);

    private final Connection connection;

    public RegisterRoute(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Response act(Request request) throws Exception {
        // provide all the necessary information to register new personal account
        // email name surname password must be provided

        Form form = new JsonForm(request.body());

        new Customer(this.connection, form.stringField("email"))
                .save(form);

        return new CreatedResponse("The registration was successful");
    }

    @Override
    public boolean accept(String path) {
        return "/register".equals(path);
    }
}
