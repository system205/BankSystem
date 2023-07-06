package oop.course.client.requests;

import oop.course.client.gui.TerminalForm;
import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.RegisterResponse;
import oop.course.client.responses.Response;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class RegisterRequest implements Request<RegisterResponse> {
    private final Request<BasicResponse> base;

    public RegisterRequest(TerminalForm terminalForm) {
        base = new JsonRequest(
                new BasicHttpRequest(Method.POST, "/register"),
                terminalForm.json()
        );
    }

    @Override
    public void send(PrintWriter printWriter) {
        base.send(printWriter);
    }

    @Override
    public RegisterResponse response(BufferedReader bufferedReader) {
        return new RegisterResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }
}
