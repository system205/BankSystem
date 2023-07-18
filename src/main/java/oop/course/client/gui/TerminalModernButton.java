package oop.course.client.gui;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Panel;

public class TerminalModernButton implements TerminalGUIElement {
    private final Button button;

    public TerminalModernButton(String text, Runnable action) {
        button = new Button(text, action);
        button.setRenderer(new Button.FlatButtonRenderer());
    }

    @Override
    public void attachTo(Panel panel) {
        panel.addComponent(button);
    }
}