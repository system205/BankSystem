package oop.course.routes.requests.methods;

import oop.course.entity.*;
import oop.course.miscellaneous.implementations.*;
import oop.course.miscellaneous.interfaces.*;
import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.routes.*;

import java.math.*;
import java.sql.*;

public final class PutRequests implements ProcessMethod {
    private final Connection connection;

    public PutRequests(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Response act(Request request) throws Exception {
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
