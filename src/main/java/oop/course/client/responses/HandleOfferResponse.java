package oop.course.client.responses;

import java.util.Objects;

public class HandleOfferResponse {
    private final BasicResponse response;

    public HandleOfferResponse(BasicResponse response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return !Objects.equals(response.raw(), "");
    }
}
