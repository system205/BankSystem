package oop.course.routes;

import oop.course.entity.*;

import oop.course.implementations.*;
import oop.course.interfaces.*;
import oop.course.responses.*;

import java.sql.*;
import java.util.*;

/**
 * List the request of all customer's accounts
 */
public class GetRequests implements ProcessMethod {
    private final Connection connection;

    public GetRequests(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Response act(Request request)  {
        return new SuccessResponse(
                new Customer(
                        this.connection,
                        new HeaderToken(
                                request.headers()
                        ).id()
                ).accounts()
                        .stream()
                        .map(Account::requests)
                        .flatMap(Collection::stream)
                        .toList()
        );
    }

    @Override
    public boolean accept(String method) {
        return "GET".equals(method);
    }
}
