package oop.course.client;


import oop.course.client.requests.*;
import oop.course.client.responses.*;

public interface ServerBridge {

    <T extends Response> T execute(Request<T> request);
}
