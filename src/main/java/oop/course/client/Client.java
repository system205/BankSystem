package oop.course.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        try {
            System.out.println(Files.walk(Paths.get(".")).filter(Files::isRegularFile).filter(p -> p.getFileName().toString().endsWith("java")).map(path -> {
                try {
                    return Files.lines(path).filter(s -> s.trim().length() > 0 && !s.trim().startsWith("/") && !s.trim().startsWith("*")).count();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).reduce(Long::sum).orElse(0L));
        } catch (IOException e) {
            System.out.println("Unable to calculate line counts, not critical, still proceeding.");
        }
        new Client(new GUIFactory(), new ServerBridgeFactory("127.0.0.1", 6666)).run();
    }

    public void run() {
        clientGui.startLooping(serverBridge);
    }
}