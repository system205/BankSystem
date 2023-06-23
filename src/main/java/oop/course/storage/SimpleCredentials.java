package oop.course.storage;

import oop.course.storage.interfaces.*;

public class SimpleCredentials implements Credentials {
    private final String username;
    private final String password;

    public SimpleCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public String password() {
        return password;
    }
}
