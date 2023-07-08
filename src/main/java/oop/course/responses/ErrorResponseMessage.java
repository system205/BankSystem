package oop.course.responses;

import oop.course.tools.JSON;

public class ErrorResponseMessage implements JSON {
    public ErrorResponseMessage(String message) {
        this.message = message;
    }

    final String message;

    @Override
    public String json() {
        return String.format("{%n\"message\":\"%s\"%n}", message);
    }
}
