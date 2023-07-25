package oop.course.routes.transfer.methods;

import oop.course.entity.*;
import oop.course.entity.transaction.*;
import oop.course.miscellaneous.implementations.*;
import oop.course.miscellaneous.interfaces.*;
import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.routes.*;

import java.sql.*;

public final class MakeTransaction implements ProcessMethod {
    private final Connection connection;

    public MakeTransaction(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Response act(Request request) throws Exception {
        Form form = new JsonForm(request.body());
        Transaction transaction = new Customer(
            this.connection,
            new HeaderToken(request.headers()).id()
        )
            .account(form.stringField("senderAccount"))
            .transfer(
                form.stringField("receiverAccount"),
                form.bigDecimalField("amount")
            );
        return new SuccessResponse(transaction.json());
    }

    @Override
    public boolean accept(String method) {
        return "PUT".equals(method);
    }
}
