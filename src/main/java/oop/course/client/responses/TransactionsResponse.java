package oop.course.client.responses;

import oop.course.client.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransactionsResponse implements Response {
    private final BasicResponse response;

    public TransactionsResponse(BasicResponse response) {
        this.response = response;
    }

    public boolean isSuccess()
    {
        return !Objects.equals(response.raw(), "");
    }

    public List<Transaction> transactions() {
        var types = response.values("type");
        var senders = response.values("from");
        var amounts = response.values("amount");
        var dates = response.values("date");
        List<Transaction> res = new ArrayList<>();
        for (int i = 0; i < amounts.size(); i++) {
            res.add(new Transaction(types.get(i), senders.get(i), amounts.get(i), dates.get(i)));
        }
        return res;
    }
}
