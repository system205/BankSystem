package oop.course.client;

public class RequestBuilder {
    private HttpJsonRequest.Method method;
    private String json;
    private String route;
    private boolean auth;

    public RequestBuilder() {
        method = HttpJsonRequest.Method.GET;
        json = "";
        route = "";
        auth = false;
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

    public RequestBuilder withToken() {
        this.auth = true;
        return this;
    }

    public HttpJsonRequest build() {
        return new HttpJsonRequest(method, json, route, auth);
    }
}