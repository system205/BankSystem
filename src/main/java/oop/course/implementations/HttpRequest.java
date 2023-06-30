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
        Collection<String> meta = new LinkedList<>();
        for (String line : data) {
            if (line.equals("")) {
                break;
            }
            meta.add(line);
        }
        return meta;
    }

    @Override
    public Iterable<String> body() {
        // a payload of the data
        Collection<String> payload = new LinkedList<>();
        boolean foundEmptyLine = false;
        for (String line : data) {
            if (line.equals("")) {
                foundEmptyLine = true;
                continue;
            }
            if (foundEmptyLine)
                payload.add(line);
        }
        return payload;
    }
}
