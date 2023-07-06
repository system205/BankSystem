package oop.course.client;

import java.io.PrintWriter;

public interface Request {

    enum Method {
        GET,
        POST
    }

    void send(PrintWriter printWriter);
}
