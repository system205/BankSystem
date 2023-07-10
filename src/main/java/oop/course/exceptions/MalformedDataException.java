package oop.course.exceptions;

public class MalformedDataException extends Exception {

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
