package oop.course;

import oop.course.auth.*;
import oop.course.server.*;

import java.io.*;
import java.net.*;

public class Main {
    public static void main(String[] args) {
        final int port = 6666;
        final Authorization authorization = new Authorization();
        try(ServerSocket socket = new ServerSocket(port)){
            while (true) new Thread(new Server(socket.accept(), authorization)).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
