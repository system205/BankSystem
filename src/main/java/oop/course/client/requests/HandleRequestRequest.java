package oop.course.client.requests;

import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.HandleRequestResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class HandleRequestRequest implements Request<HandleRequestResponse> {
    private final Request<BasicResponse> base;

    public HandleRequestRequest(String token, String form) {
        base = new JsonRequest(
                new AuthorizedRequest(
                        new BasicHttpRequest(Method.POST, "/manager/requests"),
                        token
                ),
                form
        );
    }

    @Override
    public void send(PrintWriter printWriter) {
        base.send(printWriter);
    }

    @Override
    public HandleRequestResponse response(BufferedReader bufferedReader) {
        return new HandleRequestResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }
}
