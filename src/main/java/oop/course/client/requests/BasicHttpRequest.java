package oop.course.client.requests;

import oop.course.client.responses.BasicResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class BasicHttpRequest implements Request {
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
        }
        else if (method == Method.PUT) {
            requestString += "PUT ";
        }
        requestString += route + " HTTP/1.1";
        printWriter.println(requestString);
    }

    @Override
    public BasicResponse response(BufferedReader bufferedReader) {
        var s = bufferedReader.lines().collect(Collectors.joining("\n"));
        return new BasicResponse(s);
    }
}
