package oop.course.client;

public class TerminalInputPair {
    private final TerminalText text;
    private final TerminalInputBox inputBox;

    public TerminalInputPair(TerminalText text, TerminalInputBox box) {
        this.text = text;
        this.inputBox = box;
    }

    public String json() {
        return inputBox.text();
    }
}
