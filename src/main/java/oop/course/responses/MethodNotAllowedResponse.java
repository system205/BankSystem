package oop.course.responses;

import oop.course.interfaces.Response;

import java.io.*;
import java.util.Map;

public class MethodNotAllowedResponse implements Response {
    private final String[] allowedHeaders;

    public MethodNotAllowedResponse(String[] allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }

    public MethodNotAllowedResponse() {
        this(new String[]{"GET", "HEAD", "PUT", "POST", "DELETE"});
    }


    @Override
    public void print(PrintWriter out) throws IOException {
        new BaseResponse(
                405, "Method not allowed",
                Map.ofEntries(
                        Map.entry("Allow", String.join(", ", this.allowedHeaders))
                )
        ).print(out);
    }
}
