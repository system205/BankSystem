package oop.course.client;

public class TerminalFormKeyValuePair {
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
}
