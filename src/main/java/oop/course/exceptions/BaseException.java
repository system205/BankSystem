package oop.course.exceptions;

import oop.course.tools.JSON;

public class BaseException extends RuntimeException implements JSON {
    final String message;
    public BaseException() {
       this("");
    }

    public BaseException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String json() {
        return "{%n\"message\": \"" + message +"\"%n}";
    }
}
