package oop.course.client.requests;

import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.TransferResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class TransferRequest implements Request<TransferResponse> {
    private final Request<BasicResponse> base;

    public TransferRequest(String token, String form) {
        base = new JsonRequest(new AuthorizedRequest(new BasicHttpRequest(Method.PUT, "/transfer"), token),
                form);
    }

    @Override
    public void send(PrintWriter printWriter) {
        base.send(printWriter);
    }

    @Override
    public TransferResponse response(BufferedReader bufferedReader) {
        return new TransferResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }

}