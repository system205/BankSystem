package oop.course.errors.exceptions;

public class AuthorizationException extends Exception {
    final String realm;


    public AuthorizationException(String realm, String message) {
        super(message);
        this.realm = realm;
    }

    public AuthorizationException(String message) {
        super(message);
        this.realm = "";
    }

    public AuthorizationException(String realm, String message, Throwable cause) {
        super(message, cause);
        this.realm = realm;
    }

    public String realm() {
        return realm;
    }
}
