package oop.course.implementations;

import oop.course.interfaces.*;
import oop.course.storage.interfaces.*;

public class DBLoginCheck implements CheckCredentials {
    @Override
    public boolean ok(Credentials credentials) {
        final String username = credentials.username();
        final String password = credentials.password();


        return false;
    }
}
