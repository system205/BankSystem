package oop.course.routes;

import oop.course.implementations.*;
import oop.course.interfaces.*;
import oop.course.responses.*;

import java.sql.*;

public class PutAccount implements ProcessMethod {
    private final Connection connection;

    public PutAccount(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Response act(Request request) throws Exception {
        // Create and save new account
        Account account = new CheckingAccount(
                this.connection
        );
        account.save(
                new HeaderToken(request.headers()).id()
        );
        return new SuccessResponse(
                account.json()
        );
    }

    @Override
    public boolean accept(String method) {
        return "PUT".equals(method);
    }
}
