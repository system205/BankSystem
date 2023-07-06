package oop.course.client.requests;

import oop.course.client.responses.Response;

import java.io.PrintWriter;

public interface Request<T extends Response> {

    enum Method {
        GET,
        POST
    }

    void send(PrintWriter printWriter);

    T response();
}
