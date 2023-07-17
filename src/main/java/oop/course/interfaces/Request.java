package oop.course.interfaces;


import java.util.*;

public interface Request {
    Collection<String> headers();

    Iterable<String> body();

    String url();

    String method();
}
