package oop.course.client;

import oop.course.client.requests.Request;
import oop.course.client.responses.BasicResponse;
import java.util.function.Function;

public interface GUI {
    void startLooping(Function<Request, BasicResponse> requestConsumer);
}
