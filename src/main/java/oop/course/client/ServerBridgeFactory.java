package oop.course.client;

public class ServerBridgeFactory {
    private final String ip;
    private final int port;
    public ServerBridgeFactory(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
    public ServerBridge bestServerImplementation() {
        return new SocketServerBridge(ip, port);
    }
}
