package oop.course.client.requests;

import oop.course.client.responses.*;

import java.io.*;
import java.util.stream.*;

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
