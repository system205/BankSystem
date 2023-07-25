package oop.course.client.requests;

import oop.course.client.responses.*;

import java.io.*;
import java.util.stream.*;

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
