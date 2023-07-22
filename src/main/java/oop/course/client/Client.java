package oop.course.client;

import java.io.*;

public class Client {
    private final GUI clientGui;
    private final ServerBridge serverBridge;

    public Client(GUIFactory guiFactory, ServerBridgeFactory serverFactory) throws RuntimeException {
        try {
            clientGui = guiFactory.bestGUIImplementation();
            serverBridge = serverFactory.bestServerImplementation();
        } catch (IOException e) {
            throw new RuntimeException("Unable to create GUI");
        }
    }

    public static void main(String[] args) {
        new Client(
                new GUIFactory(),
                new ServerBridgeFactory(
                        "127.0.0.1",
                        6666
                )
        ).run();
    }

    public void run() {
        clientGui.startLooping(serverBridge);
    }
}