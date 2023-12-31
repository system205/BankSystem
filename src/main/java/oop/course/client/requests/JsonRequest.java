package oop.course.client.requests;

import oop.course.client.responses.*;

import java.io.*;
import java.util.stream.*;

public final class JsonRequest implements Request<BasicResponse> {
    private final Request<BasicResponse> request;
    private final String json;

    public JsonRequest(Request<BasicResponse> request, String json) {
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
        return new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n")));
    }
}
