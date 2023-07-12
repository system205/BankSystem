package oop.course.storage.migrations;

import oop.course.tools.implementations.*;

public class MigrationDirectory {
    private final String path;

    public MigrationDirectory(String path) {
        this.path = path;
    }

    public Sql[] scan() {
        return new Sql[]{
                new Table(
                        new TextFromFile(this.path + "/init-customer-table.sql")
                ),
                new Table(
                        new TextFromFile(this.path + "/init-checking-account-table.sql")
                ),
                new Table(
                        new TextFromFile(this.path + "/init-offers-table.sql")
                ),
                new Table(
                        new TextFromFile(this.path + "/init-transaction-table.sql")
                ),
                new Table(
                        new TextFromFile(this.path + "/init-payments-table.sql")
                ),
                new Table(
                        new TextFromFile(this.path + "/init-requests-table.sql")
                ),
                new Table(
                        new TextFromFile(this.path + "/init-roles-table.sql")
                )
        };
    }
}
