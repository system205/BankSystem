package oop.course.client.gui;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.gui2.*;

public final class TerminalFixedTextBox implements TerminalInputBox {
    private final TextBox textBox;

    public TerminalFixedTextBox(String text) {
        textBox = new TextBox(new TerminalSize(20, 1));
        textBox.setText(text);
        textBox.setReadOnly(true);
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
