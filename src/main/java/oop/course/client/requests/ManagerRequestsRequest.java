package oop.course.client.requests;

import oop.course.client.responses.*;

import java.io.*;
import java.util.stream.*;

public final class ManagerRequestsRequest implements Request<ManagerRequestsResponse> {
    private final String token;

    public ManagerRequestsRequest(String token) {
        this.token = token;
    }

    @Override
    public void send(PrintWriter printWriter) {
        new AuthorizedRequest(
            new BasicHttpRequest(Method.GET, "/manager/requests"),
            token
        ).send(printWriter);
    }

    @Override
    public ManagerRequestsResponse response(BufferedReader bufferedReader) {
        return new ManagerRequestsResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }
}
