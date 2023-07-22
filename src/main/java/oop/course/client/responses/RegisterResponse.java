package oop.course.client.responses;

public class RegisterResponse implements Response {

    private final BasicResponse response;

    public RegisterResponse(BasicResponse response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return statusCode() == 201;
    }

    public String errorMessage() {
        return response.value("message");
    }

    @Override
    public int statusCode() {
        return response.statusCode();
    }
}
