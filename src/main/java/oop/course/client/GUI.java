package oop.course.client;

import oop.course.client.requests.Request;
import oop.course.client.responses.Response;

import java.util.function.Consumer;

public interface GUI {
    void startLooping(Consumer<Request<Response>> requestConsumer);
}
