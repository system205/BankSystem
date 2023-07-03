package oop.course.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketBridge implements ServerBridge {
    private final String ip;
    private final int port;
    public SocketBridge(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public Response execute(Request req) {
        try (Socket clientSocket = new Socket(ip, port);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
