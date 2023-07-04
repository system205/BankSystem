package oop.course.client;

import java.util.List;

public class TerminalForm {
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
            }
            else {
                builder.append(",\n");
            }
        }
        builder.append("}\n");
        return builder.toString();
    }
}
