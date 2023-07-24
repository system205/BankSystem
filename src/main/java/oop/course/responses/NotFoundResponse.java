package oop.course.responses;

import java.io.*;

public class NotFoundResponse implements Response {
    @Override
    public void print(PrintWriter out) {
        new BaseResponse(404, "Not Found").print(out);
    }
}
