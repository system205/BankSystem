package oop.course.routes;

import oop.course.entity.*;
import oop.course.implementations.*;
import oop.course.interfaces.*;
import oop.course.responses.*;
import oop.course.tools.implementations.*;
import oop.course.tools.interfaces.*;

import java.sql.*;

public class GetTransactions implements ProcessMethod {
    private final Connection connection;

    public GetTransactions(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Response act(Request request) {
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
