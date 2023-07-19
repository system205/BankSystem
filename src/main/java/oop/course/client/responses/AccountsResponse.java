package oop.course.client.responses;

import oop.course.client.gui.TerminalAccountsTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class AccountsResponse implements Response {
    private final BasicResponse response;

    public AccountsResponse(BasicResponse response) {
        this.response = response;
    }

    public TerminalAccountsTable accountsTable(Consumer<List<String>> onSelected) {
        var numbers = response.values("accountNumber");
        var balances = response.values("balance");
        List<List<String>> res = new ArrayList<>(numbers.size());
        for (int i = 0; i < numbers.size(); i++) {
            res.add(Arrays.asList(numbers.get(i), balances.get(i)));
        }
        return new TerminalAccountsTable(res, onSelected);
    }
}
