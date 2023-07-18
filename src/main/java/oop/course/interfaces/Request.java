package oop.course.interfaces;


import java.util.*;

public interface Request {
    Collection<String> headers() throws Exception;

    Iterable<String> body();

    String url() throws Exception;

    String method() throws Exception;
}
