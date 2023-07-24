package oop.course.routes.accounts.methods;

import oop.course.entity.*;
import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.routes.*;
import oop.course.miscellaneous.implementations.*;

import java.sql.*;

public class GetAccount implements ProcessMethod {
    private final Connection connection;

    public GetAccount(Connection connection) {
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
                                .stringField("email")
                ).json()
        );
    }

    @Override
    public boolean accept(String method) {
        return "GET".equals(method);
    }
}
