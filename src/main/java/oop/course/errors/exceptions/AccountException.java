package oop.course.errors.exceptions;

public class AccountException extends Exception {
    public AccountException() {

    }

    public AccountException(String message) {
        super(message);
    }

    public AccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
