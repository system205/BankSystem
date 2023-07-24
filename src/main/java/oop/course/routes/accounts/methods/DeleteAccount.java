package oop.course.routes.accounts.methods;

import oop.course.entity.*;
import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.routes.*;
import oop.course.miscellaneous.implementations.*;

import java.sql.*;

public class DeleteAccount implements ProcessMethod {
    private final Connection connection;

    public DeleteAccount(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Response act(Request request) throws Exception {
        new Customer(
                this.connection,
                new HeaderToken(request.headers()).id()
        ).account(new JsonForm(request.body())
                .stringField("accountNumber")
        ).deactivate();
        return new SuccessResponse(
                new ResponseMessage("Account deleted successfully").json()
        );
    }

    @Override
    public boolean accept(String method) {
        return "DELETE".equals(method);
    }
}
