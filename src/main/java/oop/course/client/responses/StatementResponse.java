package oop.course.client.responses;

import oop.course.client.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StatementResponse implements Response{
    private final BasicResponse response;

    public StatementResponse(BasicResponse response) {
        this.response = response;
    }

    public boolean isSuccess()
    {
        return !Objects.equals(response.raw(), "");
    }

    public List<Transaction> transactions() {
        var types = response.values("type");
        var senders = response.values("from").stream().skip(1).toList();
        var amounts = response.values("amount");
        var dates = response.values("date");
        List<Transaction> res = new ArrayList<>();
        for (int i = 0; i < amounts.size(); i++) {
            res.add(new Transaction(types.get(i), senders.get(i), amounts.get(i), dates.get(i)));
        }
        return res;
    }

    public String startingBalance() {
        return response.value("startingBalance");
    }

    public String endingBalance() {
        return response.value("endingBalance");
    }
}
