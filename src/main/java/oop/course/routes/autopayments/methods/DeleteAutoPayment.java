package oop.course.routes.autopayments.methods;

import oop.course.entity.*;
import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.routes.*;
import oop.course.miscellaneous.implementations.*;
import oop.course.miscellaneous.interfaces.*;

import java.sql.*;

public class DeleteAutoPayment implements ProcessMethod {
    private final Connection connection;

    public DeleteAutoPayment(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Response act(Request request) throws Exception {
        Form form = new JsonForm(request.body());
        new Customer(
                this.connection,
                new HeaderToken(request.headers()).id()
        ).deleteAutopayment(form.longField("paymentId"));
        return new SuccessResponse("No content 203");
    }

    @Override
    public boolean accept(String method) {
        return "DELETE".equals(method);
    }
}
