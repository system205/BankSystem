package oop.course.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

    private final String ip;
    private final int port;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public static void main(String[] args) {
        new Client("127.0.0.1", 6666).start();
    }

    public void start() {
        try (Socket client = new Socket(this.ip, this.port);
             PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
            System.out.println("Input text:");
            out.println(new Scanner(System.in).nextLine());
            out.println(new Scanner(System.in).nextLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
