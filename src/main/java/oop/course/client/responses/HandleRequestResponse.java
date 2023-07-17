package oop.course.client.responses;

import java.util.Objects;

public class HandleRequestResponse implements Response {
    private final BasicResponse response;

    public HandleRequestResponse(BasicResponse response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return !Objects.equals(response.raw(), "");
    }
}
