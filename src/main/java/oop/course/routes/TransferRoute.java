package oop.course.routes;

import oop.course.entity.*;
import oop.course.implementations.*;
import oop.course.interfaces.*;
import oop.course.responses.*;
import oop.course.tools.implementations.*;
import oop.course.tools.interfaces.*;

import java.sql.*;

public class TransferRoute implements Route {

    private final Connection connection;

    public TransferRoute(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Response act(Request request) {
        Form form = new JsonForm(request.body());
        Transaction transaction = new Customer(this.connection,
                new HeaderToken(request.headers()).id())
                .account(form.stringField("senderAccount"))
                .transfer(form.stringField("receiverAccount"),
                        form.bigDecimalField("amount"));

        return new SuccessResponse(transaction.info());
    }

    @Override
    public boolean accept(String path) {
        return "/transfer".equals(path);
    }
}
