package oop.course.client.requests;

import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.NewAutoPaymentResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public final class NewAutoPaymentRequest implements Request<NewAutoPaymentResponse> {
    private final String token;
    private final String form;

    public NewAutoPaymentRequest(String token, String form) {
        this.token = token;
        this.form = form;
    }

    @Override
    public void send(PrintWriter printWriter) {
        new JsonRequest(
            new AuthorizedRequest(
                new BasicHttpRequest(Method.POST, "/autopayments"),
                token
            ),
            form
        ).send(printWriter);
    }

    @Override
    public NewAutoPaymentResponse response(BufferedReader bufferedReader) {
        return new NewAutoPaymentResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }
}
