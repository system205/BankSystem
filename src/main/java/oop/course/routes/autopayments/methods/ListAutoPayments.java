package oop.course.routes.autopayments.methods;

import oop.course.entity.*;
import oop.course.requests.Request;
import oop.course.responses.*;
import oop.course.routes.*;
import oop.course.miscellaneous.implementations.*;

import java.sql.*;

public class ListAutoPayments implements ProcessMethod {
    private final Connection connection;

    public ListAutoPayments(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Response act(Request request) throws Exception {
        return new SuccessResponse(
                new Customer(
                        this.connection,
                        new HeaderToken(request.headers()).id()
                ).account(
                        new JsonForm(request.body())
                                .stringField("accountNumber")
                ).autopayments()
        );
    }

    @Override
    public boolean accept(String method) {
        return "GET".equals(method);
    }
}
