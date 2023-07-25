package oop.course.client.requests;

import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.StatementResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public final class StatementRequest implements Request<StatementResponse> {

    private final String token;
    private final String form;

    public StatementRequest(String token, String form) {
        this.token = token;
        this.form = form;
    }

    @Override
    public void send(PrintWriter printWriter) {
        new JsonRequest(
            new AuthorizedRequest(
                new BasicHttpRequest(Method.GET, "/stats"),
                token
            ),
            form
        ).send(printWriter);
    }

    @Override
    public StatementResponse response(BufferedReader bufferedReader) {
        return new StatementResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }
}
