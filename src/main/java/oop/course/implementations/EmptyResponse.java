package oop.course.implementations;

import oop.course.interfaces.*;

import java.io.*;

public class EmptyResponse implements Response {
    /**
     * Response that behaves without data
     */
    @Override
    public void print(PrintWriter out) { /* out data is supposed to be empty */ }
}
