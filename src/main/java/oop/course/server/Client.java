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

            String registerManager = """
                    POST /register HTTP/1.1

                    {
                      "email": "manager",
                      "name": "manager",
                      "surname": "manager",
                      "password": "manager"
                    }
                    EOF""";

            String loginManager = """
                    POST /login HTTP/1.1

                    {
                      "email": "manager",
                      "password": "manager"
                    }
                    EOF""";

            String login = """
                    POST /login HTTP/1.1

                    {
                      "email": "admin",
                      "password": "123"
                    }
                    EOF""";

            String managerToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJtYW5hZ2VyIiwiaWF0IjoxNjg5MzUyMjE0LCJleHAiOjE2ODk0Mzg2MTR9.sQyiIuCoeICICD7UT0yaLkzaDDQPG3nNVNp4eIDv81A";

            String loginWrongPassword = """
                    POST /login HTTP/1.1

                    {
                      "email": "admin",
                      "password": "456"
                    }
                    EOF""";

            String loginDoesntExist = """
                    POST /login HTTP/1.1

                    {
                      "email": "admin321",
                      "password": "123"
                    }
                    EOF""";

            String check = """
                    GET /account HTTP/1.1
                    Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY4ODM2NzA2OCwiZXhwIjoxNjg4NDUzNDY4fQ.UknUS-BxwCal0CurSOckSIsRk-zVsxzOGa6tttsL7AY

                    {
                      "accountNumber": "1234567890",
                    }
                    EOF""";

            String putAccount = """
                    PUT /account HTTP/1.1
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

            String allAccounts = """
                    GET /accounts HTTP/1.1
                    Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY4ODQ5MjcyOSwiZXhwIjoxNjg4NTc5MTI5fQ.UEJUjO_YYfvSZcVemm7KlxWTVwONZbVFfzCJw_h0o60
                                        
                    EOF""";

            String requests = """
                    GET /manager/requests HTTP/1.1
                    Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJtYW5hZ2VyIiwiaWF0IjoxNjg5MzUyMjE0LCJleHAiOjE2ODk0Mzg2MTR9.sQyiIuCoeICICD7UT0yaLkzaDDQPG3nNVNp4eIDv81A
                                        
                    EOF""";
            String approve = """
                    POST /manager/requests HTTP/1.1
                    Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbjIiLCJpYXQiOjE2ODg1ODE0MTgsImV4cCI6MTY4ODY2NzgxOH0.n7dzTwVM1Cwjukc4Pq8074FKU6s9m7C4ltIXqZikXYg
                            
                    {
                      "id": "2",
                      "status": "approved",
                    } 
                                
                    EOF""";

            String deny = """
                    POST /manager/requests HTTP/1.1
                    Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbjIiLCJpYXQiOjE2ODg1ODE0MTgsImV4cCI6MTY4ODY2NzgxOH0.n7dzTwVM1Cwjukc4Pq8074FKU6s9m7C4ltIXqZikXYg
                            
                    {
                      "id": "1",
                      "status": "denied",
                    } 
                                
                    EOF""";

            String checkRequests = """
                    GET /requests HTTP/1.1
                    Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbjIiLCJpYXQiOjE2ODg1ODE0MTgsImV4cCI6MTY4ODY2NzgxOH0.n7dzTwVM1Cwjukc4Pq8074FKU6s9m7C4ltIXqZikXYg
                                   
                    EOF""";

            String putRequest = """
                    PUT /requests HTTP/1.1
                    Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY4ODU3OTk2NywiZXhwIjoxNjg4NjY2MzY3fQ.ToKZ5J_5l0o6WvJBn4nFUM75sv4YHwX8ZSaWPeZxRJQ
                            
                    {
                      "amount": "200",
                      "type": "deposit",
                      "accountNumber": "8581256061"
                    }       
                    EOF""";

            String putJob = """
                    PUT /job HTTP/1.1
                    Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY4ODY1NTI1NiwiZXhwIjoxNjg4NzQxNjU2fQ.NcTHtBLxyLlPc7mHQ2DTXN0z-E1RGuBiI4tKxMvzrXY
                                   
                    EOF""";

            String offers = """
                    GET /admin/offers HTTP/1.1
                    Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbjIiLCJpYXQiOjE2ODg1ODE0MTgsImV4cCI6MTY4ODY2NzgxOH0.n7dzTwVM1Cwjukc4Pq8074FKU6s9m7C4ltIXqZikXYg
                                   
                    EOF""";
            String acceptOffer = """
                    POST /admin/offers HTTP/1.1
                    Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbjIiLCJpYXQiOjE2ODg1ODE0MTgsImV4cCI6MTY4ODY2NzgxOH0.n7dzTwVM1Cwjukc4Pq8074FKU6s9m7C4ltIXqZikXYg
                                        
                    {
                    "customerEmail" : "admin",
                    "status" : "accepted"
                    }               
                    EOF""";

            String transactions = """
                    GET /transactions HTTP/1.1
                    Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY4ODcyODcxNCwiZXhwIjoxNjg4ODE1MTE0fQ.X6Ce7oGHrz_bWNAeXsE2SOuQrwae0XHwJHe8Jpv2wsI
                                        
                    {
                    "accountNumber" : "8581256061",
                    }               
                    EOF""";
            String statement = """
                    GET /stats HTTP/1.1
                    Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY4ODcyODcxNCwiZXhwIjoxNjg4ODE1MTE0fQ.X6Ce7oGHrz_bWNAeXsE2SOuQrwae0XHwJHe8Jpv2wsI
                                        
                    {
                    "accountNumber" : "8581256061",
                    "startDate" : "2021-01-01",
                    "endDate" : "2023-09-07",
                    }               
                    EOF""";
            String deactivate = """
                    DELETE /account HTTP/1.1
                    Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY4ODgyMTE3OCwiZXhwIjoxNjg4OTA3NTc4fQ.PSUnK5JpApbph2uP68vG1zQ7aV-qpCP5XxKs8PdbzYc
                                        
                    {
                    "accountNumber" : "8821865334",
                    }               
                    EOF""";
            String autopayments = """
                    POST /autopayments HTTP/1.1
                    Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbjMiLCJpYXQiOjE2ODg5MDY5MTgsImV4cCI6MTY4ODk5MzMxOH0.JDAc8wQtdjl_Qm6zCxal7Kri4BiS3E2DKGDOqJ2q9V4                    
                                        
                    {
                    "period" : "1",
                    "senderNumber": "8907115031",
                    "receiverNumber": "8907066715",
                    "amount" : "1",
                    "startDate" : "2023-07-9",
                    "paymentId" : "3"
                    }               
                    EOF""";

            final String request = autopayments;

            System.out.println("Sent:\n" + request);
            out.println(request);
            System.out.println("\nReceived:");
            in.lines().forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
