package oop.course.implementations;


import oop.course.interfaces.*;

public class SimpleUrl implements URL {

    @Override
    public String path(Request request) {
        return request.url();
    }
}
