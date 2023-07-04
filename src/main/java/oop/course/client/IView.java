package oop.course.client;

import com.googlecode.lanterna.gui2.WindowBasedTextGUI;

import java.io.IOException;
import java.util.function.BiConsumer;

public interface IView {
    enum Type {
        Login,
        Register,
        ActionSelect,
        Account,
        Transfer,
        None
    }
    void show(WindowBasedTextGUI gui) throws IOException;

    void registerChangeViewHandler(BiConsumer<Type, String> consumer);
}
