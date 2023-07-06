package oop.course.client.requests;

import java.io.PrintWriter;

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
        requestString += route + " HTTP/1.1";
        printWriter.println(requestString);
    }
}
