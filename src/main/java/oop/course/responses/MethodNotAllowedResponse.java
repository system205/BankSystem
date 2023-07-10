package oop.course.responses;

import oop.course.interfaces.Response;

import java.io.IOException;
import java.io.PrintWriter;

// TODO - there must be the Allowed header
public class MethodNotAllowedResponse implements Response {
    @Override
    public void print(PrintWriter out) throws IOException {
        new BaseResponse(405, "Method not allowed");
    }
}
