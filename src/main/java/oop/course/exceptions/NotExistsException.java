package oop.course.exceptions;

public class NotExistsException extends Exception {
    public NotExistsException() {
    }

    public NotExistsException(String message) {
        super(message);
    }

    public NotExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
