package oop.course.client.views;

import com.googlecode.lanterna.gui2.WindowBasedTextGUI;

import java.io.IOException;

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
}
