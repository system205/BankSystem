package oop.course.routes.methods;

import oop.course.entity.*;
import oop.course.implementations.*;
import oop.course.interfaces.*;
import oop.course.responses.*;
import oop.course.routes.*;
import oop.course.tools.implementations.*;
import oop.course.tools.interfaces.*;

import java.sql.*;

public class DeleteAutoPayment implements ProcessMethod {
    private final Connection connection;

    public DeleteAutoPayment(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Response act(Request request) {
        Form form = new JsonForm(request.body());
        new Customer(this.connection, new HeaderToken(request.headers()).id())
                .deleteAutopayment(form.longField("paymentId"));
        return new SuccessResponse("No content 203");
    }

    @Override
    public boolean accept(String method) {
        return "DELETE".equals(method);
    }
}
