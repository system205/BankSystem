package oop.course.client;

import java.net.http.HttpClient;

public class HttpClientBridge implements ServerBridge {
    private final HttpClient httpClient;
    public HttpClientBridge() {
        httpClient = HttpClient.newHttpClient();
    }

    @Override
    public Response execute(Request request) {
        return null;
    }
}
