package oop.course.client.requests;

import oop.course.client.responses.*;

import java.io.*;
import java.util.stream.*;

public final class HandleRequestRequest implements Request<HandleRequestResponse> {
    private final String token;
    private final String form;

    public HandleRequestRequest(String token, String form) {
        this.token = token;
        this.form = form;
    }

    @Override
    public void send(PrintWriter printWriter) {
        new JsonRequest(
            new AuthorizedRequest(
                new BasicHttpRequest(Method.POST, "/manager/requests"),
                token
            ),
            form
        ).send(printWriter);
    }

    @Override
    public HandleRequestResponse response(BufferedReader bufferedReader) {
        return new HandleRequestResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }
}
