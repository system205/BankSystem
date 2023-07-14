package oop.course.exceptions;

public class NotExistsException extends RuntimeException {
    public NotExistsException() {
    }

    public NotExistsException(String message) {
        super(message);
    }

    public NotExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
