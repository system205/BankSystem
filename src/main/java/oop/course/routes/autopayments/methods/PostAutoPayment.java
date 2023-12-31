package oop.course.routes.autopayments.methods;

import oop.course.entity.*;
import oop.course.miscellaneous.implementations.*;
import oop.course.miscellaneous.interfaces.*;
import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.routes.*;

import java.sql.*;

public final class PostAutoPayment implements ProcessMethod {
    private final Connection connection;

    public PostAutoPayment(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Response act(Request request) throws Exception {
        Form form = new JsonForm(request.body());
        AutoPayment newPayment = new Customer(
            this.connection,
            new HeaderToken(request.headers()).id()
        ).account(form.stringField("senderNumber"))
            .createPayment(form);
        newPayment.pay(); // start up
        return new SuccessResponse(newPayment.json());
    }

    @Override
    public boolean accept(String method) {
        return "POST".equals(method);
    }
}
