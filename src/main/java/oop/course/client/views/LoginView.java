package oop.course.client.views;

import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import oop.course.client.gui.TerminalGUIElement;
import oop.course.client.gui.TerminalWindow;

import java.util.List;
import java.util.Map;

public class LoginView implements IView {
    private final TerminalWindow window;
    private final List<TerminalGUIElement> elements;

    public LoginView(WindowBasedTextGUI gui, TerminalWindow window, List<TerminalGUIElement> elements, Map<String, String> state) {
        this.window = window;
        this.elements = elements;

    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        for (var element : elements) {
            element.attachTo(window.panel());
        }
        window.addToGui(gui);
        window.open();
        window.waitUntilClosed();
    }
}
