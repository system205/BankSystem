package oop.course.client.requests;

import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.ListAutoPaymentsResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public final class ListAutoPaymentsRequest implements Request<ListAutoPaymentsResponse> {
    private final String token;
    private final String form;

    public ListAutoPaymentsRequest(String token, String form) {
        this.token = token;
        this.form = form;
    }

    @Override
    public void send(PrintWriter printWriter) {
        new JsonRequest(
            new AuthorizedRequest(
                new BasicHttpRequest(Method.GET, "/autopayments"),
                token),
            form
        ).send(printWriter);
    }

    @Override
    public ListAutoPaymentsResponse response(BufferedReader bufferedReader) {
        return new ListAutoPaymentsResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }
}
