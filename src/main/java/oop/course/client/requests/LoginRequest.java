package oop.course.client.requests;

import oop.course.client.responses.BasicResponse;
import oop.course.client.gui.TerminalForm;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class LoginRequest implements Request {
    private final Request base;

    public LoginRequest(TerminalForm terminalForm) {
        base = new JsonRequest(
                new BasicHttpRequest(Method.POST, "/login"),
                terminalForm.json()
        );
    }

    @Override
    public void send(PrintWriter printWriter) {
        base.send(printWriter);
    }

    @Override
    public BasicResponse response(BufferedReader bufferedReader) {
        return null;
    }
}
