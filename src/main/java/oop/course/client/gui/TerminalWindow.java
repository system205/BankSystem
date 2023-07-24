package oop.course.client.gui;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;

import java.util.List;

public final class TerminalWindow {
    private final BasicWindow window;

    public TerminalWindow(String title, Panel panel) {
        window = new BasicWindow(title);
        window.setHints(List.of(Window.Hint.CENTERED));
        window.setVisible(false);
        window.setComponent(panel);
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
