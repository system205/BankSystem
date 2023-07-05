package oop.course.implementations;

import oop.course.interfaces.*;

import java.io.*;
import java.util.*;

public class HttpRequest implements Request {
    private final Collection<String> data;

    public HttpRequest(BufferedReader in) throws IOException {
        this.data = new LinkedList<>();
        String line;
        while (!(line = in.readLine()).equals("EOF")) {
            this.data.add(line);
        }
        System.out.println(this.data);
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
        Collection<String> payload = new LinkedList<>();
        boolean foundEmptyLine = false;
        for (String line : data) {
            if (line.isEmpty()) {
                foundEmptyLine = true;
                continue;
            }
            if (foundEmptyLine)
                payload.add(line);
        }
        return payload;
    }

    @Override
    public String url() {
        return headers().stream()
                .findFirst().orElseThrow(() -> new RuntimeException("Http is malformed"))
                .split(" ")[1]; // the very first word
    }

    @Override
    public String method() {
        return headers().stream()
                .findFirst().orElseThrow(() -> new RuntimeException("Http is malformed"))
                .split(" ")[0]; // the very first word
    }
}
