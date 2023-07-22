package oop.course.client.responses;

import oop.course.client.gui.TerminalAccountsTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class AccountsResponse implements Response {
    private final Response response;

    public AccountsResponse(Response response) {
        this.response = response;
    }

    public TerminalAccountsTable accountsTable(Consumer<List<String>> onSelected) {
        //TODO: make rendering the table gui-independent
        var numbers = response.values("accountNumber");
        var balances = response.values("balance");
        List<List<String>> res = new ArrayList<>(numbers.length);
        for (int i = 0; i < numbers.length; i++) {
            res.add(Arrays.asList(numbers[i], balances[i]));
        }
        return new TerminalAccountsTable(res, onSelected);
    }

    @Override
    public boolean isSuccess() {
        return response.isSuccess();
    }

    @Override
    public int statusCode() {
        return response.statusCode();
    }

    @Override
    public String message() {
        return response.message();
    }

    @Override
    public String value(String key) {
        return response.value(key);
    }

    @Override
    public String[] values(String key) {
        return response.values(key);
    }

    @Override
    public String body() {
        return response.body();
    }
}
