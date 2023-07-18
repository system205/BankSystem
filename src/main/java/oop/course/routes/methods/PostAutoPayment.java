package oop.course.routes.methods;

import oop.course.entity.*;
import oop.course.implementations.*;
import oop.course.interfaces.*;
import oop.course.responses.*;
import oop.course.routes.*;
import oop.course.tools.implementations.*;
import oop.course.tools.interfaces.*;

import java.sql.*;

public class PostAutoPayment implements ProcessMethod {
    private final Connection connection;

    public PostAutoPayment(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Response act(Request request) throws Exception {
        Form form = new JsonForm(request.body());
        AutoPayment newPayment = new Customer(this.connection, new HeaderToken(request.headers()).id())
                .account(form.stringField("senderNumber"))
                .createPayment(form);
        newPayment.pay(); // start up
        return new SuccessResponse(newPayment.json());
    }

    @Override
    public boolean accept(String method) {
        return "POST".equals(method);
    }
}
