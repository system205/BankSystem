package oop.course.responses;

import oop.course.interfaces.*;
import oop.course.tools.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

public class SuccessResponse implements Response {
    private final String body;

    public SuccessResponse(String json) {
        this.body = json;
    }

    public SuccessResponse(Collection<? extends JSON> jsons) {
        this("[\n" + jsons.parallelStream().map(JSON::json)
                .collect(Collectors.joining(",\n")) +
                "\n]");
    }

    @Override
    public void print(PrintWriter out) {
        out.println(this.body);
    }
}
