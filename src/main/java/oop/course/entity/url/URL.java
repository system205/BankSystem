package oop.course.entity.url;

import oop.course.requests.*;

public interface URL {
    String path(Request request) throws Exception;
}
