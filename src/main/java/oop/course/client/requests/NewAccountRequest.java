package oop.course.client.requests;

import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.NewAccountResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public final class NewAccountRequest implements Request<NewAccountResponse> {
    private final String token;

    public NewAccountRequest(String token) {
        this.token = token;
    }

    @Override
    public void send(PrintWriter printWriter) {
        new AuthorizedRequest(
            new BasicHttpRequest(Method.PUT, "/account"),
            token
        ).send(printWriter);
    }

    @Override
    public NewAccountResponse response(BufferedReader bufferedReader) {
        return new NewAccountResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }
}
