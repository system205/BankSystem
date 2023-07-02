package oop.course.routes;

import oop.course.entity.*;
import oop.course.interfaces.*;
import oop.course.responses.*;
import oop.course.tools.implementations.*;
import oop.course.tools.interfaces.*;

import java.sql.*;

public class RegisterRoute implements Route {

    private final Connection connection;

    public RegisterRoute(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Response act(Request request) {
        // provide all the necessary information to register new personal account
        // email name surname password must be provided

        Form form = new JsonForm(request.body());
        new Customer(this.connection, form.stringField("email"))
                .save(form);

        return new CreatedResponse();
    }

    @Override
    public boolean accept(String path) {
        return "/register".equals(path);
    }
}
