package oop.course.client.responses;

import java.util.Objects;

public class BecomeManagerResponse implements Response {
    private final BasicResponse response;

    public BecomeManagerResponse(BasicResponse response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return !Objects.equals(response.raw(), "");
    }

    public String id() {
        return response.value("id");
    }
}
