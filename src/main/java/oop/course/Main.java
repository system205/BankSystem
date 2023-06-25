package oop.course;

import oop.course.auth.*;
import oop.course.server.*;

import java.io.*;
import java.net.*;
import java.nio.file.*;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println(Files.walk(Paths.get(".")).filter(Files::isRegularFile).filter(p -> p.getFileName().toString().endsWith("java")).map(path -> {try {return Files.lines(path).filter(s -> s.trim().length() > 0 && !s.trim().startsWith("/") && !s.trim().startsWith("*")).count();} catch (IOException e) {throw new RuntimeException(e);}}).reduce(Long::sum).orElse(0L));
        final int port = 6666;
        final Authorization authorization = new Authorization();
        try(ServerSocket socket = new ServerSocket(port)){
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
