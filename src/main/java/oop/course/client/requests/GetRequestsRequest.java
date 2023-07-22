package oop.course.client.requests;

import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.GetRequestsResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class GetRequestsRequest implements Request<GetRequestsResponse> {
    private final Request<BasicResponse> base;

    public GetRequestsRequest(String token) {
        base = new AuthorizedRequest(new BasicHttpRequest(Method.GET, "/requests"), token);
    }

    @Override
    public void send(PrintWriter printWriter) {
        base.send(printWriter);
    }

    @Override
    public GetRequestsResponse response(BufferedReader bufferedReader) {
        return new GetRequestsResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }
}
