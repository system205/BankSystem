package oop.course.routes;

import oop.course.entity.*;
import oop.course.entity.account.Account;
import oop.course.requests.Request;
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
    public Response act(Request request) throws Exception {
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
