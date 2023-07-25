package oop.course.client.gui;

import com.googlecode.lanterna.gui2.Panel;

public final class TerminalFormKeyValuePair implements TerminalGUIElement {
    private final String key;
    private final TerminalInputPair pair;

    public TerminalFormKeyValuePair(String key, TerminalInputPair pair) {
        this.key = key;
        this.pair = pair;
    }

    public String json() {
        return '"' + key + '"' + " : " + '"' + pair.json() + '"';
    }

    @Override
    public void attachTo(Panel panel) {
        pair.attachTo(panel);
    }
}
