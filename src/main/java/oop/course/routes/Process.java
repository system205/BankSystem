package oop.course.routes;

import oop.course.requests.*;
import oop.course.responses.*;

/**
 * The main unit of the whole system
 * <p>It takes a Request and returns the Response</p>
 */
public interface Process {
    Response act(Request request) throws Exception;
}
