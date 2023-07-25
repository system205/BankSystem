package oop.course.routes.admin.applicants.methods;

import oop.course.entity.*;
import oop.course.miscellaneous.implementations.*;
import oop.course.miscellaneous.interfaces.*;
import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.routes.*;

import java.sql.*;

public final class PostOffer implements ProcessMethod {
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
