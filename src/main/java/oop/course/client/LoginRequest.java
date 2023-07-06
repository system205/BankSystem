package oop.course.client;

import java.io.PrintWriter;

public class LoginRequest implements Request{
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
}
