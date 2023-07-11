package oop.course.client.responses;

public class NewAccountResponse implements Response {
    private final BasicResponse response;

    public NewAccountResponse(BasicResponse response) {
        this.response = response;
    }

    public String accountNumber() {
        return response.value("accountNumber");
    }

    public boolean isSuccess() {
        return response.raw().startsWith("200");
    }

    public String accountBalance() {
        return response.value("balance");
    }
}
