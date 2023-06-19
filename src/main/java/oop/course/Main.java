package oop.course;

import oop.course.server.*;

import java.io.*;
import java.net.*;

public class Main {
    public static void main(String[] args) {
        final int port = 6666;
        try(ServerSocket socket = new ServerSocket(port)){
            while (true) new Thread(new Server(socket.accept())).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
