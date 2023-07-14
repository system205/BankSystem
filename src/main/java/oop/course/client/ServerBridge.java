package oop.course.client;


import oop.course.client.requests.Request;
import oop.course.client.responses.BasicResponse;

public interface ServerBridge {

     BasicResponse execute(Request request);
}
