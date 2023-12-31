package oop.course.client.requests;

import oop.course.client.responses.*;

import java.io.*;
import java.util.stream.*;

public final class TransactionsRequest implements Request<TransactionsResponse> {
    private final String token;
    private final String form;

    public TransactionsRequest(String token, String form) {
        this.token = token;
        this.form = form;
    }

    @Override
    public void send(PrintWriter printWriter) {
        new JsonRequest(
            new AuthorizedRequest(
                new BasicHttpRequest(Method.GET, "/transactions"),
                token
            ),
            form
        ).send(printWriter);
    }

    @Override
    public TransactionsResponse response(BufferedReader bufferedReader) {
        return new TransactionsResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }
}
