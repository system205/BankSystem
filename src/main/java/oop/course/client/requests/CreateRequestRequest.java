package oop.course.client.requests;

import oop.course.client.gui.TerminalForm;
import oop.course.client.responses.BasicResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class CreateRequestRequest implements Request {
    private final Request base;

    public CreateRequestRequest(String token, TerminalForm form) {
        base = new JsonRequest(new AuthorizedRequest(new BasicHttpRequest(Method.PUT, "/requests"), token),
                form.json());
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
