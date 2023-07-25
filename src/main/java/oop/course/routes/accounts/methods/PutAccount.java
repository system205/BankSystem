package oop.course.routes.accounts.methods;

import oop.course.entity.*;
import oop.course.entity.account.*;
import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.routes.*;

import java.sql.*;

public final class PutAccount implements ProcessMethod {
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
