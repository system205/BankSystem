package oop.course.client.responses;

import java.util.Objects;

public class NewAutoPaymentResponse implements Response {
    private final BasicResponse response;

    public NewAutoPaymentResponse(BasicResponse response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return !Objects.equals(response.raw(), "");
    }
}
