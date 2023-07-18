package oop.course.server;

import oop.course.implementations.*;
import oop.course.interfaces.Process;

import java.io.*;
import java.net.*;

public class Server implements Runnable, Closeable {
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;
    private final Process process;

    public Server(ServerSocket client, Process process) {
        try {
            this.socket = client.accept();
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.process = process;
    }

    @Override
    public void run() {
        try {
            // process the request here
            this.process.act(new HttpRequest(in)).print(out);
        } catch (Exception e) {
            System.out.println("Exception when receiving a request");
        } finally {
            close();
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
