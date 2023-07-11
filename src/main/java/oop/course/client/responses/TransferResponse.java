package oop.course.client.responses;

public class TransferResponse implements Response {
    private final BasicResponse response;

    public TransferResponse(BasicResponse response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return response.raw().contains("from") && response.raw().contains("to") && response.raw().contains("amount");
    }
}
