package oop.course.client.requests;

import oop.course.client.gui.TerminalForm;
import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.NewAutoPaymentResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class NewAutoPaymentRequest implements Request<NewAutoPaymentResponse> {
    private final Request<BasicResponse> base;

    public NewAutoPaymentRequest(String token, TerminalForm form) {
        base = new JsonRequest(new AuthorizedRequest(new BasicHttpRequest(Method.POST, "/autopayments"), token),
                form.json());
    }

    @Override
    public void send(PrintWriter printWriter) {
        base.send(printWriter);
    }

    @Override
    public NewAutoPaymentResponse response(BufferedReader bufferedReader) {
        return new NewAutoPaymentResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }
}
