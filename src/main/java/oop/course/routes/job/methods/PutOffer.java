package oop.course.routes.job.methods;

import oop.course.entity.*;
import oop.course.requests.Request;
import oop.course.responses.*;
import oop.course.routes.ProcessMethod;

import java.sql.*;

public final class PutOffer implements ProcessMethod {
    private final Connection connection;

    public PutOffer(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Response act(Request request) throws Exception {
        return new SuccessResponse(
                new Customer(
                        this.connection,
                        new HeaderToken(request.headers()).id()
                ).applyForJob().json()
        );
    }

    @Override
    public boolean accept(String method) {
        return "PUT".equals(method);
    }
}
