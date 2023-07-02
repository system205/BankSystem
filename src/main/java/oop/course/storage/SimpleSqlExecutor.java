package oop.course.storage;

import oop.course.storage.migrations.*;

import java.sql.*;

public class SimpleSqlExecutor implements SqlExecutor {

    private final Connection connection;

    public SimpleSqlExecutor(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void perform(String sql) {
        try (Statement statement = this.connection.createStatement()) {
            System.out.println("Start executing: " + sql);
            statement.execute(sql);
            this.connection.commit();
            System.out.println("Committed");
        } catch (SQLException e) {
            System.err.println("SQL exception when executing: " + sql + ". Error: " + e);
            try {
                this.connection.rollback();
            } catch (SQLException ex) {
                System.out.println("Failed to rollback after commit. " + ex);
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }
}
