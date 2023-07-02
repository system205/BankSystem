package oop.course.auth.interfaces;

public interface SecurityConfiguration {
    boolean isAccessibleUrl(String url);
    boolean isValidToken(String token, String url);
}
