package oop.course.client.responses;

public final class HandleRequestResponse implements Response {
    private final Response response;

    public HandleRequestResponse(Response response) {
        this.response = response;
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
        if (isSuccess()) {
            return "Successfully handled the request";
        }
        else {
            return "Could not handle the request";
        }
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
