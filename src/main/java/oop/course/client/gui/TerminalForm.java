package oop.course.client.gui;

import com.googlecode.lanterna.gui2.Panel;

import java.util.List;

public final class TerminalForm implements TerminalGUIElement {
    private final List<TerminalFormKeyValuePair> values;

    public TerminalForm(List<TerminalFormKeyValuePair> values) {
        this.values = values;
    }

    public String json() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        int i = values.size();
        for (var value : values) {
            --i;
            builder.append(value.json());
            if (i == 0) {
                builder.append("\n");
            } else {
                builder.append(",\n");
            }
        }
        builder.append("}\n");
        return builder.toString();
    }

    @Override
    public void attachTo(Panel panel) {
        for (var element : values) {
            element.attachTo(panel);
        }
    }
}
