package oop.course.storage;

import oop.course.storage.interfaces.*;

public class SimpleNetAddress implements NetAddress {

    private final String ip;
    private final int port;

    public SimpleNetAddress(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public String ip() {
        return ip;
    }

    @Override
    public int port() {
        return port;
    }
}
