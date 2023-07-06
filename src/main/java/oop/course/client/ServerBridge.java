package oop.course.client;


import oop.course.client.requests.Request;
import oop.course.client.responses.Response;

public interface ServerBridge {
     Response execute(Request request);
}
