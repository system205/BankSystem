package oop.course.client.requests;

import oop.course.client.responses.Response;

import java.io.BufferedReader;
import java.io.PrintWriter;

public interface Request<T extends Response> {

    void send(PrintWriter printWriter);

    T response(BufferedReader bufferedReader);

    enum Method {
        GET, POST, PUT, DELETE
    }
}
