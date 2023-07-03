package oop.course.responses;

import oop.course.interfaces.*;

import java.io.*;

public class CreatedResponse implements Response {
    @Override
    public void print(PrintWriter out) {
        out.println("Created");
    }
}
