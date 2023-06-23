package oop.course.implementations;

import oop.course.interfaces.Response;

import java.io.IOException;
import java.io.Writer;

public class ForbiddenResponse implements Response {
    private final String message;

    public ForbiddenResponse(String message) {
        this.message = message;
    }

    @Override
    public void print(Writer out) throws IOException {
        out.write("Forbidden Response: " + message);
    }
}
