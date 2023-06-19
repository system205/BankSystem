package oop.course.server;

import java.io.*;
import java.net.*;

public class Server implements Runnable, Closeable {
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;

    public Server(Socket client) {
        this.socket = client;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try { // process the request here
            System.out.println(in.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
