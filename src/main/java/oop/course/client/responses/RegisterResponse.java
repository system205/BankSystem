package oop.course.client.responses;

import java.util.Objects;

public class RegisterResponse implements Response {

    private final BasicResponse response;

    public RegisterResponse(BasicResponse response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return Objects.equals(response.raw(), "Created");
    }

    @Override
    public int statusCode() {
        return response.statusCode();
    }
}
