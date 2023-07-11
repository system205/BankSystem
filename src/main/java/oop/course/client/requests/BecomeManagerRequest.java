package oop.course.client.requests;

import oop.course.client.responses.BasicResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class BecomeManagerRequest implements Request{
    private final Request base;

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
    public BasicResponse response(BufferedReader bufferedReader) {
        return new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n")));
    }
}
