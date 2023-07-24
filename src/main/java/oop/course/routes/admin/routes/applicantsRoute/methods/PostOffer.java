package oop.course.routes.admin.routes.applicantsRoute.methods;

import oop.course.entity.*;
import oop.course.requests.Request;
import oop.course.responses.*;
import oop.course.routes.ProcessMethod;
import oop.course.tools.implementations.*;
import oop.course.tools.interfaces.*;

import java.sql.*;

public class PostOffer implements ProcessMethod {
    private final Connection connection;

    public PostOffer(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Response act(Request request) throws Exception {
        Form form = new JsonForm(request.body());
        new Customer(
                this.connection,
                form.stringField("customerEmail")
        ).offer().update(
                form.stringField("status")
        );
        return new SuccessResponse(new ResponseMessage("Offer is review successfully").json());
    }

    @Override
    public boolean accept(String method) {
        return "POST".equals(method);
    }
}