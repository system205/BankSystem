package oop.course.client.responses;

public interface Response {
    boolean isSuccess();
    int statusCode();
    String body();
    String message();
    String value(String key);
    String[] values(String key);
}
