package oop.course.client;

import oop.course.client.requests.Request;
import oop.course.client.responses.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketServerBridge implements ServerBridge {
    private final String ip;
    private final int port;

    public SocketServerBridge(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public <T extends Response> T execute(Request<T> request) {
        try (Socket client = new Socket(
                this.ip,
                this.port);
             PrintWriter out = new PrintWriter(
                     client.getOutputStream(),
                     true
             );
             BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))
        ) {
            request.send(out);
            out.println("EOF");
            return request.response(in);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
