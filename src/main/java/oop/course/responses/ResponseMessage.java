package oop.course.responses;

import oop.course.miscellaneous.JSON;

public class ResponseMessage implements JSON {
    public ResponseMessage(String message) {
        this.message = message;
    }

    private final String message;

    @Override
    public String json() {
        return String.format("{%n\"message\":\"%s\"%n}", message);
    }
}
