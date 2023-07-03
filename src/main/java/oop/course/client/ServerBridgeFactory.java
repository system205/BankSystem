package oop.course.client;

public class ServerBridgeFactory {
    public ServerBridge bestServerImplementation() {
        return new SocketServerBridge();
    }
}
