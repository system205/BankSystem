package oop.course.client.requests;

import oop.course.client.responses.BasicResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class JsonRequest implements Request {
    private final Request request;
    private final String json;

    public JsonRequest(Request request, String json) {
        this.request = request;
        this.json = json;
    }


    @Override
    public void send(PrintWriter printWriter) {
        request.send(printWriter);
        printWriter.println();
        printWriter.println(json);
    }

    @Override
    public BasicResponse response(BufferedReader bufferedReader) {
        return null;
    }
}
