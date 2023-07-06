package oop.course.client.gui;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Panel;
import oop.course.client.gui.TerminalGUIElement;

public class TerminalButton implements TerminalGUIElement {
    private final Button button;
    public TerminalButton(String text, Runnable action){
        button = new Button(text, action);
    }

    @Override
    public void attachTo(Panel panel) {
        panel.addComponent(button);
    }
}
