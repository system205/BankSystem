package oop.course.routes;

import oop.course.entity.*;
import oop.course.interfaces.*;
import oop.course.responses.*;
import oop.course.storage.*;
import oop.course.tools.implementations.*;

public class RegisterRoute implements Route {

    private final Database<Long, Customer> database;

    public RegisterRoute(Database<Long, Customer> database) {
        this.database = database;
    }

    @Override
    public Response act(Request request) {
        // provide all the necessary information to register new personal account
        // email name surname password must be provided

        new Customer(
                new JsonForm(
                        request.body()
                )
        ).save(this.database);

        return new CreatedResponse();
    }

    @Override
    public boolean accept(String path) {
        return "/register".equals(path);
    }
}
