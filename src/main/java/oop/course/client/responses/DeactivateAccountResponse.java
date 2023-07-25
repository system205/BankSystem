package oop.course.client.responses;

public final class DeactivateAccountResponse implements Response {
    private final Response response;

    public DeactivateAccountResponse(Response response) {
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
            return "Successfully deactivated an account";
        } else {
            return "Could not deactivate an account";
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
