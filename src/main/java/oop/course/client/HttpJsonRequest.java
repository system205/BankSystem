package oop.course.client;

import java.io.PrintWriter;

public class HttpJsonRequest {
    private final Method method;
    private final String json;
    private final String route;
    private final boolean auth;

    HttpJsonRequest(Method method, String json, String route, boolean auth) {
        this.method = method;
        this.json = json;
        this.route = route;
        this.auth = auth;
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
        if (auth) {
            //add auth header
            httpText.append("Token: 123321\n");
        }
        httpText.append("\n");
        httpText.append(json);
        String req = httpText.toString();
        printWriter.print(req);
    }

    public Object sendWithToken(PrintWriter printWriter, String token) {
        return null;
    }

    public enum Method {
        GET,
        POST
    }
}
