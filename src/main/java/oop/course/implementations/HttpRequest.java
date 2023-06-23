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
        for (String line : this.data) {
            if (line.isEmpty()) break;
            headers.add(line);
        }
        return headers;
    }

    @Override
    public Iterable<String> body() {
        // a payload of the data
        ArrayList<String> payload = new ArrayList<>();
        boolean flag = false;
        for (String line : data) {
            if (!line.isEmpty() && !flag) continue;
            else if (line.isEmpty() && !flag) flag = true;
            if (!line.isEmpty()) payload.add(line);
        }
        return payload;
    }
}
