package oop.course.client.requests;

import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.TransferResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public final class TransferRequest implements Request<TransferResponse> {
    private final String token;
    private final String form;

    public TransferRequest(String token, String form) {
        this.token = token;
        this.form = form;
    }

    @Override
    public void send(PrintWriter printWriter) {
        new JsonRequest(
            new AuthorizedRequest(
                new BasicHttpRequest(Method.PUT, "/transfer"),
                token
            ),
            form
        ).send(printWriter);
    }

    @Override
    public TransferResponse response(BufferedReader bufferedReader) {
        return new TransferResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }

}