package oop.course.storage.migrations;

import oop.course.tools.implementations.*;

public class MigrationDirectory {
    private final String path;

    public MigrationDirectory(String path) {
        this.path = path;
    }

    public Sql[] scan() {
        return new Sql[]{
                new CostumerTable(
                        new TextFromFile(this.path + "/init-customer-table.sql")
                ),
                new CheckingAccountTable(
                        new TextFromFile(this.path + "/init-checking-account-table.sql")
                ),
                new RolesTable(
                        new TextFromFile(this.path + "/init-roles-table.sql")
                )
        };
    }
}
