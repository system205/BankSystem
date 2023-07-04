package oop.course.client;

import com.googlecode.lanterna.gui2.Panel;

public class TerminalInputPair implements TerminalGUIElement {
    private final TerminalText text;
    private final TerminalInputBox inputBox;

    public TerminalInputPair(TerminalText text, TerminalInputBox box) {
        this.text = text;
        this.inputBox = box;
    }

    public String json() {
        return inputBox.text();
    }

    @Override
    public void attachTo(Panel panel) {
        text.attachTo(panel);
        inputBox.attachTo(panel);
    }
}
