package oop.course.interfaces;

import oop.course.exceptions.MalformedDataException;

public interface URL {
    String path(Request request) throws MalformedDataException;
}
