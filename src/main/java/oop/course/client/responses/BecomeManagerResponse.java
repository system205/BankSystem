package oop.course.client.responses;

public class BecomeManagerResponse implements Response {
    private final BasicResponse response;

    public BecomeManagerResponse(BasicResponse response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return !response.raw().contains("Bad Request");
    }

    public String id() {
        return response.value("id");
    }
    @Override
    public int statusCode() {
        return response.statusCode();
    }
}
