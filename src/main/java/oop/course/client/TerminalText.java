package oop.course.client;

import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;

public class TerminalText implements TerminalGUIElement {
    private final Label textLabel;
    public TerminalText(String text)
    {
        textLabel = new Label(text);
    }

    @Override
    public void attachTo(Panel panel)
    {
        panel.addComponent(textLabel);
    }
}
