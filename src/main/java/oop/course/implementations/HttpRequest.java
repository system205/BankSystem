package oop.course.implementations;

import oop.course.interfaces.*;

import java.util.*;
import java.util.stream.*;

public class HttpRequest implements Request {
    private final Iterable<String> data;

    public HttpRequest(Stream<String> lines) {
        this.data = lines.toList();
    }

    @Override
    public Collection<String> headers() {
        // a part of the data
        Collection<String> headers = new ArrayList<>();
        return headers;
    }

    @Override
    public Iterable<String> body() {
        // a payload of the data
        Iterable<String> payload = new ArrayList<>();
        return payload;
    }
}
