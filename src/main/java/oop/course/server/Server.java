package oop.course.server;

import oop.course.interfaces.Process;
import oop.course.requests.HttpRequest;
import org.slf4j.*;

import java.io.*;
import java.net.*;

public class Server implements Runnable, Closeable {
    private static final Logger log = LoggerFactory.getLogger(Server.class);
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;
    private final Process process;

    public Server(ServerSocket client, Process process) {
        log.debug("Server is accepting a client");
        try {
            this.socket = client.accept();
            this.out = new PrintWriter(
                    socket.getOutputStream(),
                    true
            );
            this.in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()
                    )
            );
            log.info("New client is accepted");
        } catch (IOException e) {
            log.error("Error when accepting a client", e);
            throw new RuntimeException("Client is not accepted");
        }

        this.process = process;
    }

    @Override
    public void run() {
        try {
            log.debug("Start client processing");
            this.process.act(
                    new HttpRequest(in)
            ).print(out);
        } catch (Exception e) {
            log.error("Unhandled error when processing a client", e);
        } finally {
            close();
        }
    }

    public void close() {
        try {
            log.trace("Closing input and output streams of a client");
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            log.error("Exception when closing client's connection", e);
        }
    }
}
