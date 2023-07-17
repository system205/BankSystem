package oop.course.client.requests;

import oop.course.client.responses.BasicResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;

public interface Request {

    void send(PrintWriter printWriter);

    BasicResponse response(BufferedReader bufferedReader);

    enum Method {
        GET, POST, PUT, DELETE
    }
}
