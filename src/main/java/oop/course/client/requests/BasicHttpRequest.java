package oop.course.client.requests;

import oop.course.client.responses.*;

import java.io.*;
import java.util.stream.*;

public final class BasicHttpRequest implements Request<BasicResponse> {
    private final String route;
    private final Method method;

    public BasicHttpRequest(Method method, String route) {
        this.route = route;
        this.method = method;
    }


    @Override
    public void send(PrintWriter printWriter) {
        String requestString = "";
        if (method == Method.GET) {
            requestString += "GET ";
        } else if (method == Method.POST) {
            requestString += "POST ";
        } else if (method == Method.PUT) {
            requestString += "PUT ";
        } else if (method == Method.DELETE) {
            requestString += "DELETE ";
        }
        requestString += route + " HTTP/1.1";
        printWriter.println(requestString);
    }

    @Override
    public BasicResponse response(BufferedReader bufferedReader) {
        return new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n")));
    }
}
