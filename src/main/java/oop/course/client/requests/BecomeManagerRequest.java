package oop.course.client.requests;

import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.BecomeManagerResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public final class BecomeManagerRequest implements Request<BecomeManagerResponse> {
    private final String token;

    public BecomeManagerRequest(String token) {
        this.token = token;
    }

    @Override
    public void send(PrintWriter printWriter) {
        new AuthorizedRequest(
            new BasicHttpRequest(Method.PUT, "/job"),
            token
        ).send(printWriter);
    }

    @Override
    public BecomeManagerResponse response(BufferedReader bufferedReader) {
        return new BecomeManagerResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }
}
