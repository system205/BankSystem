package oop.course.client.requests;

import oop.course.client.responses.*;

import java.io.*;

public interface Request<T extends Response> {

    void send(PrintWriter printWriter);

    T response(BufferedReader bufferedReader);

    enum Method {
        GET, POST, PUT, DELETE
    }
}
