package oop.course.client.requests;

import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.ListAutoPaymentsResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class ListAutoPaymentsRequest implements Request<ListAutoPaymentsResponse> {
    private final Request<BasicResponse> base;

    public ListAutoPaymentsRequest(String token, String form) {
        base = new JsonRequest(new AuthorizedRequest(new BasicHttpRequest(Method.GET, "/autopayments"), token),
                form);
    }

    @Override
    public void send(PrintWriter printWriter) {
        base.send(printWriter);
    }

    @Override
    public ListAutoPaymentsResponse response(BufferedReader bufferedReader) {
        return new ListAutoPaymentsResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }
}
