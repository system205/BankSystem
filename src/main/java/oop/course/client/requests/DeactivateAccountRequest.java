package oop.course.client.requests;

import oop.course.client.gui.TerminalForm;
import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.DeactivateAccountResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class DeactivateAccountRequest implements Request<DeactivateAccountResponse> {
    private final Request<BasicResponse> base;

    public DeactivateAccountRequest(String token, TerminalForm terminalForm) {
        base = new JsonRequest(new AuthorizedRequest(new BasicHttpRequest(Method.DELETE, "/account"), token),
                terminalForm.json());
    }

    @Override
    public void send(PrintWriter printWriter) {
        base.send(printWriter);
    }

    @Override
    public DeactivateAccountResponse response(BufferedReader bufferedReader) {
        return new DeactivateAccountResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }
}
