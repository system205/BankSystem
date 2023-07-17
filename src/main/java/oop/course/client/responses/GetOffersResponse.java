package oop.course.client.responses;

import oop.course.client.Transaction;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class GetOffersResponse implements Response {
    private final BasicResponse response;

    public GetOffersResponse(BasicResponse response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return !Objects.equals(response.raw(), "");
    }

    public List<Transaction> transactions() {
        return Collections.emptyList();
    }
}
