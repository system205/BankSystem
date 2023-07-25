package oop.course.client.requests;

import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.GetOffersResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public final class GetOffersRequest implements Request<GetOffersResponse> {
    private final String token;

    public GetOffersRequest(String token) {
        this.token = token;
    }

    @Override
    public void send(PrintWriter printWriter) {
        new AuthorizedRequest(
            new BasicHttpRequest(Method.GET, "/admin/offers"),
            token
        ).send(printWriter);
    }

    @Override
    public GetOffersResponse response(BufferedReader bufferedReader) {
        return new GetOffersResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }
}
