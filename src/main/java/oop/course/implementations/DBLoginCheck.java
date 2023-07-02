package oop.course.implementations;

import oop.course.interfaces.*;
import oop.course.storage.interfaces.*;

import java.sql.*;

public class DBLoginCheck implements CheckCredentials {

    private final Connection connection;

    public DBLoginCheck(Connection connection) {
        this.connection = connection;
    }


    @Override
    public boolean ok(Credentials credentials) {
        final String username = credentials.username();
        final String password = credentials.password();


        try (PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM customer WHERE name = ? AND password = ?")) {
            statement.setString(1, username);
            statement.setString(2, password);
            return statement.executeQuery().next();
        } catch (SQLException e) {
            System.out.println("Exception when checking credentials in DB");
            throw new RuntimeException(e);
        }
    }
}
