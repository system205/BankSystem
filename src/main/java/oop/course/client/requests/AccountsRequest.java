package oop.course.client.requests;

import oop.course.client.responses.AccountsResponse;
import oop.course.client.responses.BasicResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public final class AccountsRequest implements Request<AccountsResponse> {
    private final String token;

    public AccountsRequest(String token) {
        this.token = token;
    }

    @Override
    public void send(PrintWriter printWriter) {
        new AuthorizedRequest(
            new BasicHttpRequest(Method.GET, "/accounts"),
            token
        ).send(printWriter);
    }

    @Override
    public AccountsResponse response(BufferedReader bufferedReader) {
        return new AccountsResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }

}
