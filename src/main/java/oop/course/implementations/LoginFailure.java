package oop.course.implementations;

import oop.course.interfaces.*;

import java.io.*;

public class LoginFailure implements Response {
    private final String message;

    public LoginFailure(String message) {
        this.message = message;
    }


    @Override
    public void print(PrintWriter out) {
        out.println(this.message);
    }
}
