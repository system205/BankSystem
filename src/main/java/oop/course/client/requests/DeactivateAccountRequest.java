package oop.course.client.requests;

import oop.course.client.responses.*;

import java.io.*;
import java.util.stream.*;

public final class DeactivateAccountRequest implements Request<DeactivateAccountResponse> {
    private final String token;
    private final String form;

    public DeactivateAccountRequest(String token, String form) {
        this.token = token;
        this.form = form;
    }

    @Override
    public void send(PrintWriter printWriter) {
        new JsonRequest(
            new AuthorizedRequest(
                new BasicHttpRequest(Method.DELETE, "/account"),
                token),
            form
        ).send(printWriter);
    }

    @Override
    public DeactivateAccountResponse response(BufferedReader bufferedReader) {
        return new DeactivateAccountResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }
}
