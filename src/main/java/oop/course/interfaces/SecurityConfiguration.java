package oop.course.interfaces;

public interface SecurityConfiguration {
    boolean isAccessibleUrl(String url);
    boolean isValidToken(String token);
}
