package oop.course.interfaces;

import java.io.IOException;

public interface URL {
    String path(Request request) throws IOException;
}
