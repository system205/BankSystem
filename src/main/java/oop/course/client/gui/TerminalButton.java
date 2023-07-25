package oop.course.client.gui;

import com.googlecode.lanterna.gui2.*;

public final class TerminalButton implements TerminalGUIElement {
    private final Button button;

    public TerminalButton(String text, Runnable action) {
        button = new Button(text, action);
    }

    @Override
    public void attachTo(Panel panel) {
        panel.addComponent(button);
    }
}
