package oop.course.interfaces;

import oop.course.exceptions.MalformedDataException;

public interface Id<T> {
    T id() throws Exception;
}
