package oop.course.client.requests;

import oop.course.client.gui.TerminalForm;
import oop.course.client.responses.BasicResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class RegisterRequest implements Request {
    private final Request base;

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
    public BasicResponse response(BufferedReader bufferedReader) {
        return new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n")));
    }
}