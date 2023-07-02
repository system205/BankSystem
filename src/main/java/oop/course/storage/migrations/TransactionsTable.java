package oop.course.storage.migrations;

import oop.course.tools.interfaces.*;

public class TransactionsTable implements Sql {
    private final Source source;

    public TransactionsTable(Source source) {
        this.source = source;
    }

    @Override
    public String query() {
        return this.source.text();
    }
}
