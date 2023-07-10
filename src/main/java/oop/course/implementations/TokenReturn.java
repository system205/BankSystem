package oop.course.implementations;

import oop.course.entity.*;
import oop.course.exceptions.MalformedDataException;
import oop.course.interfaces.*;
import oop.course.interfaces.Process;
import oop.course.responses.*;
import oop.course.tools.implementations.*;
import oop.course.tools.interfaces.*;

import java.sql.*;

public class TokenReturn implements Process {
    private final String secretKey;
    private final Connection connection;
    private final long expiration;

    public TokenReturn(String secretKey, long expireIn, Connection connection) {
        this.secretKey = secretKey;
        this.connection = connection;
        this.expiration = expireIn;
    }

    @Override
    public Response act(Request request) throws MalformedDataException {
        Form form = new JsonForm(request.body());
        return new SuccessResponse(
                new Customer(
                        this.connection,
                        form
                ).token(secretKey,
                        form.stringField("password"),
                        this.expiration
                ).json()
        );
    }
}
