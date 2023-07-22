package oop.course.client.responses;

import java.util.Objects;

public class DeactivateAccountResponse implements Response {
    private final BasicResponse response;

    public DeactivateAccountResponse(BasicResponse response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return Objects.equals(response.raw(), "OK");
    }
    @Override
    public int statusCode() {
        return response.statusCode();
    }
}
