package oop.course.client.requests;

import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.TransactionsResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class TransactionsRequest implements Request<TransactionsResponse> {
    private final Request<BasicResponse> base;

    public TransactionsRequest(String token, String form) {
        base = new JsonRequest(
                new AuthorizedRequest(
                        new BasicHttpRequest(Method.GET, "/transactions"),
                        token
                ),
                form
        );
    }

    @Override
    public void send(PrintWriter printWriter) {
        base.send(printWriter);
    }

    @Override
    public TransactionsResponse response(BufferedReader bufferedReader) {
        return new TransactionsResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }
}
