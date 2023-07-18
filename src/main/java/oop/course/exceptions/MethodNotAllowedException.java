package oop.course.exceptions;

public class MethodNotAllowedException extends Exception {
    public MethodNotAllowedException() {
    }

    public MethodNotAllowedException(String message) {
        super(message);
    }

    public MethodNotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodNotAllowedException(Throwable cause) {
        super(cause);
    }
}
