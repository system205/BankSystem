package oop.course.client.requests;

import oop.course.client.gui.TerminalForm;
import oop.course.client.responses.BasicResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class DeleteAutoPaymentRequest implements Request {
    private final Request base;

    public DeleteAutoPaymentRequest(String token, TerminalForm form) {
        base = new JsonRequest(new AuthorizedRequest(new BasicHttpRequest(Method.DELETE, "/autopayments"), token),
                form.json());
    }

    @Override
    public void send(PrintWriter printWriter) {
        base.send(printWriter);
    }

    @Override
    public BasicResponse response(BufferedReader bufferedReader) {
        return new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n")));
    }
}
