package oop.course.routes.requests.methods;

import oop.course.entity.*;
import oop.course.entity.account.*;
import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.routes.*;

import java.sql.*;
import java.util.*;

/**
 * List the request of all customer's accounts
 */
public final class GetRequests implements ProcessMethod {
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
                .map(account -> {
                    try { // return requests of each account
                        return account.requests();
                    } catch (Exception e) {
                        return Collections.<Account>emptyList();
                    }
                })
                .flatMap(Collection::stream)
                .toList()
        );
    }

    @Override
    public boolean accept(String method) {
        return "GET".equals(method);
    }
}
