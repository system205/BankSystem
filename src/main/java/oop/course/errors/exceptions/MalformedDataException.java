package oop.course.errors.exceptions;

/**
 * Exception that is thrown when the request is malformed: wrong format, missing parameters, etc.
 */
public final class MalformedDataException extends Exception {

    public MalformedDataException() {
        super();
    }

    public MalformedDataException(String message) {
        super(message);
    }

    public MalformedDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
