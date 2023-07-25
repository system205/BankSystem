package oop.course.storage;

import oop.course.storage.interfaces.*;
import oop.course.miscellaneous.interfaces.*;

public final class SimpleCredentials implements Credentials {
    private final String username;
    private final String password;

    public SimpleCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public SimpleCredentials(Form form) throws Exception {
        this(form.stringField("email"), form.stringField("password"));
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
