package oop.course.client;

import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;

public class TerminalPasswordBox {
    private final TextBox textBox;
    public TerminalPasswordBox()
    {
        textBox = new TextBox();
        textBox.setMask('*');
    }

    void attachTo(Panel panel)
    {
        panel.addComponent(textBox);
    }
}
