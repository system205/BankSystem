package oop.course.implementations;

import oop.course.interfaces.Process;
import oop.course.interfaces.*;
import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.tools.implementations.*;

import java.sql.*;

public class AccountReturn implements Process {


    private final String tableName;
    private final Connection connection;

    public AccountReturn(String tableName, Connection connection) {
        this.tableName = tableName;
        this.connection = connection;
    }

    @Override
    public Response act(Request request) {
        return new CheckingResponse(
                new CheckingAccount(
                        new AccountRequest(
                                new JsonForm(request.body())
                        ).id(),
                        this.tableName,
                        this.connection
                ).json()
        );
    }
}
