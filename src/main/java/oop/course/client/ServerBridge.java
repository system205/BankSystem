package oop.course.client;

public interface ServerBridge {
    HttpJsonResponse execute(HttpJsonRequest request);
}
