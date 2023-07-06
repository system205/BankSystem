package oop.course.storage.migrations;

import oop.course.tools.interfaces.*;

public class Table implements Sql {
    private final Source source;

    public Table(Source source) {
        this.source = source;
    }

    @Override
    public String query() {
        return this.source.text();
    }
}
