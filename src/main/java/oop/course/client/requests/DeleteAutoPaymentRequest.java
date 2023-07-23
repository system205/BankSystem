package oop.course.client.requests;

import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.DeleteAutoPaymentResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class DeleteAutoPaymentRequest implements Request<DeleteAutoPaymentResponse> {
    private final Request<BasicResponse> base;

    public DeleteAutoPaymentRequest(String token, String form) {
        base = new JsonRequest(new AuthorizedRequest(new BasicHttpRequest(Method.DELETE, "/autopayments"), token),
                form);
    }

    @Override
    public void send(PrintWriter printWriter) {
        base.send(printWriter);
    }

    @Override
    public DeleteAutoPaymentResponse response(BufferedReader bufferedReader) {
        return new DeleteAutoPaymentResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }
}
