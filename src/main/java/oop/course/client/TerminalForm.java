package oop.course.client;

import java.util.List;

public class TerminalForm {
    private final String method;
    private final List<TerminalFormKeyValuePair> values;
    public TerminalForm(String method, List<TerminalFormKeyValuePair> values) {
        this.method = method;
        this.values = values;
    }

    public String json() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        for (var value : values) {
            builder.append(value.json());
        }
        builder.append("}\n");
        return builder.toString();
    }
}
