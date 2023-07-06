package oop.course.client;

import java.io.PrintWriter;

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
}
