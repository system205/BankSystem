package oop.course.client.responses;

import java.util.Objects;

public class LoginResponse implements Response {
    private final BasicResponse response;

    public LoginResponse(BasicResponse response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return Objects.equals(response.raw(), "Created");
    }

    public boolean isWrongCredentials() {
        return Objects.equals(response.raw(), "Wrong credentials");
    }

    public String token() {
        return response.value("token");
    }
}
