package oop.course.responses;

import oop.course.interfaces.*;

import java.io.*;

public class SuccessResponse implements Response {
    private final String body;

    public SuccessResponse(String json) {
        this.body = json;
    }

    @Override
    public void print(PrintWriter out) {
        out.println(this.body);
    }
}
