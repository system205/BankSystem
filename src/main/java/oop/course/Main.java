package oop.course;

import oop.course.auth.*;
import oop.course.implementations.*;
import oop.course.routes.*;
import oop.course.server.*;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println(Files.walk(Paths.get(".")).filter(Files::isRegularFile).filter(p -> p.getFileName().toString().endsWith("java")).map(path -> {
            try {
                return Files.lines(path).filter(s -> s.trim().length() > 0 && !s.trim().startsWith("/") && !s.trim().startsWith("*")).count();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).reduce(Long::sum).orElse(0L));
        final int port = 6666;
        final Authorization authorization = new Authorization(
                Optional.of(new Fork(
                        new FakeUrl(),
                        new MainRoute(),
                        new LoginRoute()
                )));
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
