package oop.course.client.gui;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;

import java.util.List;

public final class TerminalWindow {
    private final BasicWindow window;
    private final Panel contentPanel;

    public TerminalWindow(String title, Panel panel) {
        this.window = new BasicWindow(title);
        this.window.setHints(List.of(Window.Hint.CENTERED));
        this.window.setVisible(false);
        this.window.setComponent(panel);
        this.contentPanel = panel;
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

    public Panel panel() {
        return contentPanel;
    }
}
