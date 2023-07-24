package oop.course.routes;

import oop.course.entity.*;
import oop.course.requests.Request;
import oop.course.responses.*;

import java.sql.*;

public class ListApplicants implements ProcessMethod {
    private final Connection connection;

    public ListApplicants(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Response act(Request request) throws Exception {
        return new SuccessResponse(new Admin(this.connection).offers());
    }

    @Override
    public boolean accept(String method) {
        return "GET".equals(method);
    }
}
