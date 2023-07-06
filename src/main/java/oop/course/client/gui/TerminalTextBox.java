package oop.course.client.gui;

import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;

public class TerminalTextBox implements TerminalInputBox {
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

    @Override
    public String text() {
        return textBox.getText();
    }
}
