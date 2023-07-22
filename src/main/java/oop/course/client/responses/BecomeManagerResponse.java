package oop.course.client.responses;

public class BecomeManagerResponse implements Response {
    private final Response response;

    public BecomeManagerResponse(Response response) {
        this.response = response;
    }

    public String id() {
        return value("id");
    }

    @Override
    public boolean isSuccess() {
        return response.isSuccess();
    }

    @Override
    public int statusCode() {
        return response.statusCode();
    }

    @Override
    public String message() {
        return response.message();
    }

    @Override
    public String value(String key) {
        return response.value(key);
    }

    @Override
    public String[] values(String key) {
        return response.values(key);
    }

    @Override
    public String body() {
        return response.body();
    }
}
