package oop.course.client;

import java.io.PrintWriter;

public class HttpJsonRequest {
    private final Method method;
    private final String json;
    private final String route;
    private final boolean auth;

    private HttpJsonRequest(Method method, String json, String route, boolean auth) {
        this.method = method;
        this.json = json;
        this.route = route;
        this.auth = auth;
    }

    public Object sendWithToken(PrintWriter printWriter, String token) {
        return null;
    }

    public enum Method {
        GET,
        POST
    }

    public class RequestBuilder {
        private Method method;
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
}
