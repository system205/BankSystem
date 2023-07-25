package oop.course.routes.transfer.methods;

import oop.course.entity.*;
import oop.course.entity.transaction.Transaction;
import oop.course.requests.Request;
import oop.course.responses.*;
import oop.course.routes.ProcessMethod;
import oop.course.miscellaneous.implementations.*;
import oop.course.miscellaneous.interfaces.*;

import java.sql.*;

public class MakeTransaction implements ProcessMethod {
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
