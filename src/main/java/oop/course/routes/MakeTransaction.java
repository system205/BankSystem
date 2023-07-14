package oop.course.routes;

import oop.course.entity.*;
import oop.course.exceptions.MalformedDataException;
import oop.course.implementations.*;
import oop.course.interfaces.*;
import oop.course.responses.*;
import oop.course.tools.implementations.*;
import oop.course.tools.interfaces.*;

import java.sql.*;

public class MakeTransaction implements ProcessMethod {

    private final Connection connection;

    public MakeTransaction(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Response act(Request request) throws Exception {
        Form form = new JsonForm(request.body());
        try {
            Transaction transaction = new Customer(this.connection,
                    new HeaderToken(request.headers()).id())
                    .account(form.stringField("senderAccount"))
                    .transfer(form.stringField("receiverAccount"),
                            form.bigDecimalField("amount"));
            return new SuccessResponse(transaction.json());
        } catch (IllegalStateException e) {
            return new BadRequestResponse("Not enough money on account to make transaction");
        }

    }

    @Override
    public boolean accept(String method) {
        return "PUT".equals(method);
    }
}
