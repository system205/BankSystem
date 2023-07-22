package oop.course.client.responses;

public class LoginResponse implements Response {
    private final BasicResponse response;

    public LoginResponse(BasicResponse response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return statusCode() == 200;
    }

    public String errorMessage() {
        return response.value("message");
    }

    public String token() {
        return response.value("token");
    }

    @Override
    public int statusCode() {
        return response.statusCode();
    }
}
