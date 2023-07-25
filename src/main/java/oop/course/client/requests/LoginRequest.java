package oop.course.client.requests;

import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.LoginResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public final class LoginRequest implements Request<LoginResponse> {
    private final String form;

    public LoginRequest(String form) {
        this.form = form;
    }

    @Override
    public void send(PrintWriter printWriter) {
        new JsonRequest(
            new BasicHttpRequest(Method.POST, "/login"),
            form
        ).send(printWriter);
    }

    @Override
    public LoginResponse response(BufferedReader bufferedReader) {
        return new LoginResponse(
                new BasicResponse(
                        bufferedReader.lines().collect(Collectors.joining("\n"))
                )
        );
    }
}
