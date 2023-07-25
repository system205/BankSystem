package oop.course.client.requests;

import oop.course.client.responses.*;

import java.io.*;
import java.util.stream.*;

public final class HandleOfferRequest implements Request<HandleOfferResponse> {
    private final String token;
    private final String form;

    public HandleOfferRequest(String token, String form) {
        this.token = token;
        this.form = form;
    }

    @Override
    public void send(PrintWriter printWriter) {
        new JsonRequest(
            new AuthorizedRequest(
                new BasicHttpRequest(Method.POST, "/admin/offers"),
                token
            ),
            form
        ).send(printWriter);
    }

    @Override
    public HandleOfferResponse response(BufferedReader bufferedReader) {
        return new HandleOfferResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }
}
