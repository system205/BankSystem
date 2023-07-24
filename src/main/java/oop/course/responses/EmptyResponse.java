package oop.course.responses;

import java.io.*;

public class EmptyResponse implements Response {
    /**
     * Response that behaves without data
     */
    @Override
    public void print(PrintWriter out) { /* out data is supposed to be empty */ }
}
