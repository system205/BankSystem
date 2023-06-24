package oop.course.storage.migrations;

import oop.course.tools.inerfaces.*;

public class CostumerTable implements Sql {
    private final Source source;

    public CostumerTable(Source source) {
        this.source = source;
    }

    @Override
    public String query() {
        return this.source.text();
    }
}
