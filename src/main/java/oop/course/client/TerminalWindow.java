package oop.course.client;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;

import java.util.List;

public class TerminalWindow {
    private final BasicWindow window;

    public TerminalWindow(String title) {
        window = new BasicWindow(title);
        window.setHints(List.of(Window.Hint.CENTERED));
        window.setVisible(false);
    }

    public void close() {
        window.close();
    }

    public void open() {
        window.setVisible(true);
    }

    public void setContent(Panel panel) {
        window.setComponent(panel);
    }

    public void addToGui(WindowBasedTextGUI gui) {
        gui.addWindow(window);
    }
}
