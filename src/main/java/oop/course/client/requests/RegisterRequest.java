package oop.course.client.requests;

import oop.course.client.responses.*;

import java.io.*;
import java.util.stream.*;

public final class RegisterRequest implements Request<RegisterResponse> {
    private final String form;

    public RegisterRequest(String form) {
        this.form = form;
    }

    @Override
    public void send(PrintWriter printWriter) {
        new JsonRequest(
            new BasicHttpRequest(Method.POST, "/register"),
            form
        ).send(printWriter);
    }

    @Override
    public RegisterResponse response(BufferedReader bufferedReader) {
        return new RegisterResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }
}
