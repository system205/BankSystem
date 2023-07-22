package oop.course.client.responses;

import java.util.Objects;

public class DeleteAutoPaymentResponse implements Response {
    private final BasicResponse response;

    public DeleteAutoPaymentResponse(BasicResponse response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return !Objects.equals(response.raw(), "");
    }
    @Override
    public int statusCode() {
        return response.statusCode();
    }
}
