package oop.course.client.responses;

import oop.course.client.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountsResponse implements Response {
    private final BasicResponse response;

    public AccountsResponse(BasicResponse response) {
        this.response = response;
    }

    public List<Account> accounts() {
        var numbers = response.values("accountNumber");
        var balances = response.values("balance");
        List<Account> res = new ArrayList<>(numbers.size());
        for (int i = 0; i < numbers.size(); i++) {
            res.add(new Account(numbers.get(i), balances.get(i)));
        }
        return res;
    }
}
