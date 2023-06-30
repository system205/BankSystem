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

    private void start() {
        try (Socket client = new Socket(this.ip, this.port);
             PrintWriter out = new PrintWriter(client.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
            String register = """
                    POST /register HTTP/1.1

                    {
                      "username": "admin2",
                      "password": "admin2"
                    }
                    EOF""";

            String login = """
                    POST /login HTTP/1.1

                    {
                      "username": "admin2",
                      "password": "admin2"
                    }
                    EOF""";

            String check = """
                    POST /account HTTP/1.1

                    {
                      "username": "admin",
                      "password": "admin"
                    }
                    EOF""";

            String transfer = """
                    POST /transfer HTTP/1.1

                    {
                      "senderAccount": "admin",
                      "receiverAccount": "admin"
                    }
                    EOF""";

            String random = """
                    POST /random HTTP/1.1
                    EOF""";

            final String request = register;

            System.out.println("Sent:\n" + request);
            out.println(request);
            System.out.println("\nReceived:");
            in.lines().forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
