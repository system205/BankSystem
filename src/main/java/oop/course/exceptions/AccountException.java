package oop.course.exceptions;

public class AccountException extends RuntimeException {
    public AccountException() {

    }

    public AccountException(String message) {
        super(message);
    }

    public AccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
