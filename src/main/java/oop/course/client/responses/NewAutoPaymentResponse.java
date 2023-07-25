package oop.course.client.responses;

public final class NewAutoPaymentResponse implements Response {
    private final Response response;

    public NewAutoPaymentResponse(Response response) {
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
            return "Successfully created an autopayment";
        }
        else {
            return "Could not create an autopayment";
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
