package oop.course.client.requests;

import oop.course.client.responses.*;

import java.io.*;
import java.util.stream.*;

public final class GetRequestsRequest implements Request<GetRequestsResponse> {
    private final String token;

    public GetRequestsRequest(String token) {
        this.token = token;
    }

    @Override
    public void send(PrintWriter printWriter) {
        new AuthorizedRequest(
            new BasicHttpRequest(Method.GET, "/requests"),
            token
        ).send(printWriter);
    }

    @Override
    public GetRequestsResponse response(BufferedReader bufferedReader) {
        return new GetRequestsResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }
}
