package oop.course.storage.migrations;

import oop.course.tools.interfaces.*;

public class RolesTable implements Sql {
    private final Source source;

    public RolesTable(Source source) {
        this.source = source;
    }

    @Override
    public String query() {
        return source.text();
    }
}
