package oop.course.routes;

import oop.course.entity.*;
import oop.course.implementations.*;
import oop.course.interfaces.*;
import oop.course.responses.*;
import oop.course.tools.implementations.*;
import oop.course.tools.interfaces.*;

import java.math.*;
import java.sql.*;

public class PutRequests implements ProcessMethod {
    private final Connection connection;

    public PutRequests(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Response act(Request request) {
        Form form = new JsonForm(request.body());
        BigDecimal amount = form.bigDecimalField("amount");
        String type = form.stringField("type");

        CustomerRequest customerRequest =
                new Customer(
                        this.connection,
                        new HeaderToken(
                                request.headers()
                        ).id()
                ).account(
                        form.stringField("accountNumber")
                ).attachRequest(type, amount);

        return new SuccessResponse(
                customerRequest.json()
        );
    }

    @Override
    public boolean accept(String method) {
        return "PUT".equals(method);
    }
}
