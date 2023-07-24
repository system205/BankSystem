package oop.course.interfaces;

import oop.course.requests.Request;
import oop.course.responses.Response;

/**
 * The main unit of the whole system
 * <p>It takes a Request and returns the Response</p>
 */
public interface Process {
    Response act(Request request) throws Exception;
}
