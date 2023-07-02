package oop.course.implementations;

import oop.course.interfaces.*;

public class SimpleUrl implements URL {

    @Override
    public String path(Request request) {
        return request.headers().stream().findFirst().orElseThrow(() -> new RuntimeException("No URL")).split(" ")[1];
    }
}
