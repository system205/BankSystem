package oop.course.client;

import java.io.*;

public final class Client {
    private final GUI clientGui;
    private final ServerBridge serverBridge;

    public Client(GUI clientGui, ServerBridge serverBridge) {
        this.clientGui = clientGui;
        this.serverBridge = serverBridge;
    }

    public static void main(String[] args) throws IOException {
        new Client(
            new TerminalGUI(),
            new SocketServerBridge(
                "127.0.0.1",
                6666
            )
        ).run();
    }

    public void run() {
        clientGui.startLooping(serverBridge);
    }
}