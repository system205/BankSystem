package oop.course.client.requests;

import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.BecomeManagerResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public final class BecomeManagerRequest implements Request<BecomeManagerResponse> {
    private final Request<BasicResponse> base;

    public BecomeManagerRequest(String token) {
        base = new AuthorizedRequest(
                new BasicHttpRequest(Method.PUT, "/job"),
                token
        );
    }

    @Override
    public void send(PrintWriter printWriter) {
        base.send(printWriter);
    }

    @Override
    public BecomeManagerResponse response(BufferedReader bufferedReader) {
        return new BecomeManagerResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }
}
