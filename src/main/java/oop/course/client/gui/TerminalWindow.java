package oop.course.client.gui;

import com.googlecode.lanterna.gui2.*;

import java.util.*;

public final class TerminalWindow {
    private final BasicWindow window;

    public TerminalWindow(String title, Panel panel, TerminalGUIElement... elements) {
        this.window = new BasicWindow(title);
        this.window.setHints(List.of(Window.Hint.CENTERED));
        this.window.setVisible(false);
        for (var element : elements) {
            element.attachTo(panel);
        }
        this.window.setComponent(panel);
    }

    public void close() {
        window.close();
    }

    public void open() {
        window.setVisible(true);
    }

    public void addToGui(WindowBasedTextGUI gui) {
        gui.addWindow(window);
    }

    public void waitUntilClosed() {
        window.waitUntilClosed();
    }
}
