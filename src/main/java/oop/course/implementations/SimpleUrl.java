package oop.course.implementations;

import oop.course.exceptions.MalformedDataException;
import oop.course.interfaces.*;

public class SimpleUrl implements URL {

    @Override
    public String path(Request request) throws MalformedDataException {
        return request.url();
    }
}
