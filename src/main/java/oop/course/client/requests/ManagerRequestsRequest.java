package oop.course.client.requests;

import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.ManagerRequestsResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public final class ManagerRequestsRequest implements Request<ManagerRequestsResponse> {
    private final Request<BasicResponse> base;

    public ManagerRequestsRequest(String token) {
        base = new AuthorizedRequest(
                new BasicHttpRequest(Method.GET, "/manager/requests"),
                token
        );
    }

    @Override
    public void send(PrintWriter printWriter) {
        base.send(printWriter);
    }

    @Override
    public ManagerRequestsResponse response(BufferedReader bufferedReader) {
        return new ManagerRequestsResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }
}
