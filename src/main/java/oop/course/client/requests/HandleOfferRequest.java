package oop.course.client.requests;

import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.HandleOfferResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public final class HandleOfferRequest implements Request<HandleOfferResponse> {
    private final Request<BasicResponse> base;

    public HandleOfferRequest(String token, String form) {
        base = new JsonRequest(
                new AuthorizedRequest(
                        new BasicHttpRequest(Method.POST, "/admin/offers"),
                        token
                ),
                form
        );
    }

    @Override
    public void send(PrintWriter printWriter) {
        base.send(printWriter);
    }

    @Override
    public HandleOfferResponse response(BufferedReader bufferedReader) {
        return new HandleOfferResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }
}
