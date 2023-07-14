package oop.course.implementations;

import oop.course.entity.*;
import oop.course.exceptions.MalformedDataException;
import oop.course.interfaces.*;
import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.routes.*;
import oop.course.tools.implementations.*;

import java.sql.*;

public class GetAccount implements ProcessMethod {
    private final Connection connection;

    public GetAccount(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Response act(Request request) throws Exception {
        return new CheckingResponse(
                new Customer(this.connection,
                        new HeaderToken(request.headers()).id())
                        .account(new AccountRequest(
                                new JsonForm(request.body()))
                                .id()
                        ).json()
        );
    }

    @Override
    public boolean accept(String method) {
        return "GET".equals(method);
    }
}
