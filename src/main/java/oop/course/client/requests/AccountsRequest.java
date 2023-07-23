package oop.course.client.requests;

import oop.course.client.responses.AccountsResponse;
import oop.course.client.responses.BasicResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class AccountsRequest implements Request<AccountsResponse> {
    private final Request<BasicResponse> base;

    public AccountsRequest(String token) {
        base = new AuthorizedRequest(
                new BasicHttpRequest(Method.GET, "/accounts"),
                token
        );
    }

    @Override
    public void send(PrintWriter printWriter) {
        base.send(printWriter);
    }

    @Override
    public AccountsResponse response(BufferedReader bufferedReader) {
        return new AccountsResponse(new BasicResponse(bufferedReader.lines().collect(Collectors.joining("\n"))));
    }

}
