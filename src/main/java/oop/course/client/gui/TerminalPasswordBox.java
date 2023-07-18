package oop.course.client.gui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;

public class TerminalPasswordBox implements TerminalInputBox {
    private final TextBox textBox;

    public TerminalPasswordBox() {
        textBox = new TextBox(new TerminalSize(20, 1));
        textBox.setMask('*');
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
