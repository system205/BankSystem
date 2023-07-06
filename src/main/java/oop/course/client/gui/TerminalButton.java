package oop.course.client.gui;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Panel;

public class TerminalButton implements TerminalGUIElement {
    private final Button button;
    public TerminalButton(String text, Runnable action){
        button = new Button(text, action);
    }

    public TerminalButton(String text, Runnable action, boolean modern){
        button = new Button(text, action);
        if (modern) {
            button.setRenderer(new Button.FlatButtonRenderer());
        }
    }

    @Override
    public void attachTo(Panel panel) {
        panel.addComponent(button);
    }
}
