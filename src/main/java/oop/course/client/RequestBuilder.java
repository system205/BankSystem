package oop.course.client;

public class RequestBuilder {
    private HttpJsonRequest.Method method;
    private String json;
    private String route;
    private String token;

    public RequestBuilder() {
        method = HttpJsonRequest.Method.GET;
        json = "";
        route = "";
        token = null;
    }

    public RequestBuilder withGet() {
        method = HttpJsonRequest.Method.GET;
        return this;
    }

    public RequestBuilder withPost() {
        method = HttpJsonRequest.Method.POST;
        return this;
    }

    public RequestBuilder withRoute(String route) {
        this.route = route;
        return this;
    }

    public RequestBuilder withJson(String json) {
        this.json = json;
        return this;
    }

    public RequestBuilder withToken(String token) {
        this.token = token;
        return this;
    }

    public HttpJsonRequest build() {
        return new HttpJsonRequest(method, json, route, token);
    }
}