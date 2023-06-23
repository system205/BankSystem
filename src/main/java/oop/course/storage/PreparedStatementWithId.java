package oop.course.storage;

import java.sql.*;

public class PreparedStatementWithId {
    private final PreparedStatement statement;
    public PreparedStatementWithId(Connection connection, String query, long id) {
        try {
            this.statement = connection.prepareStatement(query);
            this.statement.setLong(1, id);
        } catch (SQLException e) {
            final String error = "Failed to prepare statement with id. Internal error: " + e;
            System.err.println(error);
            throw new RuntimeException(e);
        }
    }

    public ResultSet execute() {
        try {
            return this.statement.executeQuery();
        } catch (SQLException e) {
            final String error = "Failed to execute preparedStatement with id. Internal error: " + e;
            System.err.println(error);
            throw new RuntimeException(e);
        }
    }
}
