package oop.course.interfaces;

import oop.course.exceptions.MalformedDataException;

import java.util.*;

public interface Request {

    Collection<String> headers() throws MalformedDataException;

    Iterable<String> body();

    String url() throws MalformedDataException;

    String method() throws MalformedDataException;
}
