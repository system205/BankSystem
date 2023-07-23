package oop.course.client.requests;

import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.CreateRequestResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class CreateRequestRequest implements Request<CreateRequestResponse> {
    private final Request<BasicResponse> base;

    public CreateRequestRequest(String token, String form) {
        base = new JsonRequest(
                new AuthorizedRequest(
                        new BasicHttpRequest(Method.PUT, "/requests"),
                        token),
                form
        );
    }

    @Override
    public void send(PrintWriter printWriter) {
        base.send(printWriter);
    }

    @Override
    public CreateRequestResponse response(BufferedReader bufferedReader) {
        return new CreateRequestResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }
}
