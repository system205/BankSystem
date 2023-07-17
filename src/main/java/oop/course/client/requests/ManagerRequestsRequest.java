package oop.course.client.requests;

import oop.course.client.responses.BasicResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class ManagerRequestsRequest implements Request {
    private final Request base;

    public ManagerRequestsRequest(String token) {
        base = new AuthorizedRequest(new BasicHttpRequest(Method.GET, "/manager/requests"), token);
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
