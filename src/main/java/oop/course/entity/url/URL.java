package oop.course.entity.url;

import oop.course.requests.Request;

public interface URL {
    String path(Request request) throws Exception;
}
