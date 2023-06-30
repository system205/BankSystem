package oop.course.responses;

import oop.course.interfaces.*;

import java.io.*;

public class CheckingResponse implements Response {
    private final String payload;

    public CheckingResponse(String body) {
        this.payload = body;
    }


    @Override
    public void print(PrintWriter out) {
        out.println("200 OK");
        out.println(this.payload);
    }
}
