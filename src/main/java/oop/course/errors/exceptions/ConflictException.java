package oop.course.errors.exceptions;

public class ConflictException extends Exception {
    public ConflictException() {
    }

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
