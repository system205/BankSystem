package oop.course.client.gui;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.gui2.*;

public final class TerminalTextBox implements TerminalInputBox {
    private final TextBox textBox;

    public TerminalTextBox() {
        textBox = new TextBox(new TerminalSize(20, 1));
    }

    @Override
    public void attachTo(Panel panel) {
        panel.addComponent(textBox);
    }

    @Override
    public String text() {
        return textBox.getText();
    }
}
