package oop.course.client;


import oop.course.client.requests.Request;
import oop.course.client.responses.Response;

public interface ServerBridge {

     <T extends Response> T execute(Request<T> request);
}
