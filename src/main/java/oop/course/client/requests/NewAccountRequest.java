package oop.course.client.requests;

import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.NewAccountResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class NewAccountRequest implements Request<NewAccountResponse> {
    private final Request<BasicResponse> base;

    public NewAccountRequest(String token) {
        base = new AuthorizedRequest(new BasicHttpRequest(Method.PUT, "/account"), token);
    }

    @Override
    public void send(PrintWriter printWriter) {
        base.send(printWriter);
    }

    @Override
    public NewAccountResponse response(BufferedReader bufferedReader) {
        return new NewAccountResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }
}
