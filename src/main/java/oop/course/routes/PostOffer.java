package oop.course.routes;

import oop.course.entity.*;

import oop.course.interfaces.*;
import oop.course.responses.*;
import oop.course.tools.implementations.*;
import oop.course.tools.interfaces.*;

import java.sql.*;

public class PostOffer implements ProcessMethod {
    private final Connection connection;

    public PostOffer(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Response act(Request request)  {
        Form form = new JsonForm(request.body());
        new Customer(this.connection, form.stringField("customerEmail"))
                .offer().update(
                        form.stringField("status")
                );
        return new SuccessResponse("Offer is review successfully");
    }

    @Override
    public boolean accept(String method) {
        return "POST".equals(method);
    }
}
