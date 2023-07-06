package oop.course.client.gui;

import com.googlecode.lanterna.gui2.Panel;
import oop.course.client.gui.TerminalGUIElement;
import oop.course.client.gui.TerminalInputPair;

public class TerminalFormKeyValuePair implements TerminalGUIElement {
    private final String key;
    private final TerminalInputPair pair;
    public TerminalFormKeyValuePair(String key, TerminalInputPair pair) {
        this.key = key;
        this.pair = pair;
    }

    public String json() {
        StringBuilder builder = new StringBuilder();
        builder.append('"').append(key).append('"');
        builder.append(" : ");
        builder.append('"').append(pair.json()).append('"');
        return builder.toString();
    }

    @Override
    public void attachTo(Panel panel) {
        pair.attachTo(panel);
    }
}
