package oop.course.client;

import java.io.IOException;
import java.util.function.Consumer;

public interface IView {
    enum Type
    {
        Login,
        Register,
        Account,
        Transfer,
        None
    }
    void show() throws IOException;

    void registerChangeViewHandler(Consumer<Type> consumer);
}
