package oop.course.client;

import java.io.IOException;

public interface View {
    enum Type
    {
        Login,
        Register,
        Account,
        Transfer,
        None
    }
    Type show() throws IOException;
}
