package oop.course.client.responses;

import java.util.Objects;

public class CreateRequestResponse implements Response {
    private final BasicResponse response;

    public CreateRequestResponse(BasicResponse response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return !Objects.equals(response.raw(), "");
    }
}
