package oop.course.client.requests;

import oop.course.client.responses.BasicResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class AuthorizedRequest implements Request {
    private final Request request;
    private final String token;

    public AuthorizedRequest(Request request, String token) {
        this.request = request;
        this.token = token;
    }


    @Override
    public void send(PrintWriter printWriter) {
        request.send(printWriter);
        String requestString = "Authorization: Bearer " + token;
        printWriter.println(requestString);
    }

    @Override
    public BasicResponse response(BufferedReader bufferedReader) {
        return new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n")));
    }
}
