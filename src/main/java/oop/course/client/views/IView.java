package oop.course.client.views;

import com.googlecode.lanterna.gui2.WindowBasedTextGUI;

import java.io.IOException;

public interface IView {
    void show(WindowBasedTextGUI gui) throws IOException;
}
