package oop.course.client.requests;

import oop.course.client.responses.*;

import java.io.*;
import java.util.stream.*;

public final class DeleteAutoPaymentRequest implements Request<DeleteAutoPaymentResponse> {
    private final String token;
    private final String form;

    public DeleteAutoPaymentRequest(String token, String form) {
        this.token = token;
        this.form = form;
    }

    @Override
    public void send(PrintWriter printWriter) {
        new JsonRequest(
            new AuthorizedRequest(
                new BasicHttpRequest(Method.DELETE, "/autopayments"),
                token),
            form
        ).send(printWriter);
    }

    @Override
    public DeleteAutoPaymentResponse response(BufferedReader bufferedReader) {
        return new DeleteAutoPaymentResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }
}
