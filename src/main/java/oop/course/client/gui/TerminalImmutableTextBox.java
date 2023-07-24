package oop.course.client.gui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;

public final class TerminalImmutableTextBox implements TerminalInputBox {
    private final TextBox textBox;

    public TerminalImmutableTextBox(String text) {
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
