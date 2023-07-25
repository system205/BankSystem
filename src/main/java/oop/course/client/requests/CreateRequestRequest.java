package oop.course.client.requests;

import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.CreateRequestResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public final class CreateRequestRequest implements Request<CreateRequestResponse> {
    private final String token;
    private final String form;

    public CreateRequestRequest(String token, String form) {
        this.token = token;
        this.form = form;
    }

    @Override
    public void send(PrintWriter printWriter) {
        new JsonRequest(
            new AuthorizedRequest(
                new BasicHttpRequest(Method.PUT, "/requests"),
                token),
            form
        ).send(printWriter);
    }

    @Override
    public CreateRequestResponse response(BufferedReader bufferedReader) {
        return new CreateRequestResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }
}
