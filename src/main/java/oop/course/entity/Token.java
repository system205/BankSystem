package oop.course.entity;

import oop.course.miscellaneous.*;

public final class Token implements JSON {
    private final String value;

    public Token(String value) {
        this.value = value;
    }

    @Override
    public String json() {
        return String.format("{%n\"token\":\"%s\"%n}", this.value);
    }
}
