package oop.course.client.responses;

public class TransferResponse implements Response {
    private final BasicResponse response;

    public TransferResponse(BasicResponse response) {
        this.response = response;
    }
}
