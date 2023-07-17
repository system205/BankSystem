package oop.course.client.responses;

import oop.course.client.BankRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ManagerRequestsResponse implements Response {
    private final BasicResponse response;

    public ManagerRequestsResponse(BasicResponse response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return !Objects.equals(response.raw(), "");
    }

    public List<BankRequest> requests() {
        var ids = response.values("id");
        var numbers = response.values("accountNumber");
        var amounts = response.values("amount");
        var types = response.values("type");
        var statuses = response.values("status");
        List<BankRequest> res = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            res.add(new BankRequest(ids.get(i), numbers.get(i), amounts.get(i), types.get(i), statuses.get(i)));
        }
        return res;
    }
}
