package oop.course.client;

import com.googlecode.lanterna.gui2.WindowBasedTextGUI;

import java.io.IOException;
import java.util.function.Consumer;

public interface IView {
    enum Type {
        Login,
        Register,
        Account,
        Transfer,
        None
    }
    void show(WindowBasedTextGUI gui) throws IOException;

    void registerChangeViewHandler(Consumer<Type> consumer);
}
