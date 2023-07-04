package oop.course.client;

import java.io.PrintWriter;

public class HttpJsonRequest {
    private final Method method;
    private final String json;
    private final String route;
    private final String token;

    HttpJsonRequest(Method method, String json, String route, String token) {
        this.method = method;
        this.json = json;
        this.route = route;
        this.token = token;
    }

    public void send(PrintWriter printWriter) {
        StringBuilder httpText = new StringBuilder();
        if (method == Method.GET) {
            httpText.append("GET ");
        }
        else {
            httpText.append("POST ");
        }
        httpText.append(route).append(" HTTP/1.1\n");
        if (token != null) {
            //add auth header
            httpText.append("Authorization: Bearer ").append(token);
        }
        httpText.append("\n");
        httpText.append(json);
        httpText.append("EOF");
        String req = httpText.toString();
        printWriter.println(req);
    }

    public Object sendWithToken(PrintWriter printWriter, String token) {
        return null;
    }

    public enum Method {
        GET,
        POST
    }
}
