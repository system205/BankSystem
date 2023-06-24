package oop.course.storage.migrations;

import oop.course.tools.inerfaces.*;

public class CheckingAccountTable implements Sql {
    private final Source source;

    public CheckingAccountTable(Source source) {
        this.source = source;
    }

    @Override
    public String query() {
        return this.source.text();
    }
}
