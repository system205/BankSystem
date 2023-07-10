package oop.course.exceptions;

import java.io.IOException;

/**
 * Exception that is thrown when the request is malformed: wrong format, missing parameters, etc.
 */
public class MalformedDataException extends IOException {

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
