package oop.course.server;

import java.io.*;
import java.net.*;

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
                      "email": "admin",
                      "name": "admin",
                      "surname": "admin",
                      "password": "123"
                    }
                    EOF""";

            String login = """
                    POST /login HTTP/1.1

                    {
                      "email": "admin",
                      "password": "123"
                    }
                    EOF""";

            String check = """
                    GET /accounts HTTP/1.1
                    Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY4ODM2NzA2OCwiZXhwIjoxNjg4NDUzNDY4fQ.UknUS-BxwCal0CurSOckSIsRk-zVsxzOGa6tttsL7AY

                    {
                      "accountNumber": "1234567890",
                    }
                    EOF""";

            String putAccount = """
                    PUT /accounts HTTP/1.1
                    Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY4ODM2NzA2OCwiZXhwIjoxNjg4NDUzNDY4fQ.UknUS-BxwCal0CurSOckSIsRk-zVsxzOGa6tttsL7AY

                    EOF""";

            String transfer = """
                    PUT /transfer HTTP/1.1
                    Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY4ODM2OTkwMSwiZXhwIjoxNjg4NDU2MzAxfQ.8GqmuA22hLQVBISaHZ7hTkSbsuUlBtTaLwZ9D3vzEUQ

                    {
                      "senderAccount": "admin",
                      "receiverAccount": "admin",
                      "amount": "100"
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
