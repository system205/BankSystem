package oop.course.client;

import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;

public class TerminalTextBox {
    private final TextBox textBox;
    public TerminalTextBox()
    {
        textBox = new TextBox();
    }

    void attachTo(Panel panel)
    {
        panel.addComponent(textBox);
    }
}
