package oop.course.client.responses;

public final class DeleteAutoPaymentResponse implements Response {
    private final Response response;

    public DeleteAutoPaymentResponse(Response response) {
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
            return "Successfully deleted an autopayment";
        }
        else {
            return "Could not delete an autopayment";
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
