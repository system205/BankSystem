package oop.course.client;

import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;

public class TerminalTextBox implements TerminalGUIElement {
    private final TextBox textBox;
    public TerminalTextBox()
    {
        textBox = new TextBox();
    }

    @Override
    public void attachTo(Panel panel)
    {
        panel.addComponent(textBox);
    }
}
