package oop.course.exceptions;

public class InternalErrorException extends Exception {
    public InternalErrorException() {
    }

    public InternalErrorException(String message) {
        super(message);
    }

    public InternalErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
