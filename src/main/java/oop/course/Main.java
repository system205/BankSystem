package oop.course;

import oop.course.auth.*;
import oop.course.implementations.*;
import oop.course.routes.*;
import oop.course.server.*;
import oop.course.storage.*;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
//        System.out.println(Files.walk(Paths.get(".")).filter(Files::isRegularFile).filter(p -> p.getFileName().toString().endsWith("java")).map(path -> {try {return Files.lines(path).filter(s -> s.trim().length() > 0 && !s.trim().startsWith("/") && !s.trim().startsWith("*")).count();} catch (IOException e) {throw new RuntimeException(e);}}).reduce(Long::sum).orElse(0L));

        Connection connection = new Postgres(
                new SimpleNetAddress("127.0.0.1", 5432),
                new SimpleCredentials("postgres", "postgres"),
                "bank"
        ).connect();
        connection.setAutoCommit(false);

        // Processes
        final Authorization authorization = new Authorization(
                Optional.of(new Fork(
                        new SimpleUrl(),
                        new MainRoute(),
                        new LoginRoute(
                                new DBLoginCheck(connection)
                        ),
                        new RegisterRoute(),
                        new TransferRoute(),
                        new CheckAccountRoute(
                                new AccountAccess( // either Forbidden or proceed
                                        new AccountReturn(
                                                new CheckingAccountDB()
                                        )
                                )
                        ),
                        new NotFoundRoute()
                )));

        final int port = 6666;
        try (ServerSocket socket = new ServerSocket(port)) {
            while (true)
                new Thread(
                        new Server(
                                socket.accept(),
                                authorization))
                        .start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
