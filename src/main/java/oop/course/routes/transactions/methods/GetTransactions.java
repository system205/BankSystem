package oop.course.routes.transactions.methods;

import oop.course.entity.*;
import oop.course.requests.Request;
import oop.course.responses.*;
import oop.course.routes.ProcessMethod;
import oop.course.tools.implementations.*;
import oop.course.tools.interfaces.*;

import java.sql.*;

public class GetTransactions implements ProcessMethod {
    private final Connection connection;

    public GetTransactions(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Response act(Request request) throws Exception {
        Form form = new JsonForm(request.body());
        return new SuccessResponse(
                new Customer(
                        this.connection,
                        new HeaderToken(request.headers()).id()
                ).account(form.stringField("accountNumber"))
                        .transactions()
        );
    }

    @Override
    public boolean accept(String method) {
        return "GET".equals(method);
    }
}