package oop.course.storage.migrations;

import oop.course.miscellaneous.interfaces.*;

public final class Table implements Sql {
    private final Source source;

    public Table(Source source) {
        this.source = source;
    }

    @Override
    public String query() {
        return this.source.text();
    }
}
