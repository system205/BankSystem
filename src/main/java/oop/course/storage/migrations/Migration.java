package oop.course.storage.migrations;

import java.sql.*;

public class Migration implements Initializer {

    private final Connection connection;

    public Migration(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void init() {
        try (final Statement statement = this.connection.createStatement();) {
            // customer table
            statement.execute("CREATE TABLE IF NOT EXISTS customer (id SERIAL PRIMARY KEY, email VARCHAR(255) NOT NULL," +
                    " name VARCHAR(255) NOT NULL, surname VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL);");

            // sample customer data
//            statement.execute("INSERT INTO customer(email, name, surname, password) " + "VALUES ('a@b.c', 'a', 'b', 'c')");

            // checking account table
            statement.execute("CREATE TABLE IF NOT EXISTS checking_account (account_id SERIAL PRIMARY KEY," +
                    " customer_id INT NOT NULL," +
                    " bank_name VARCHAR(255) NOT NULL," +
                    " account_number CHAR(10) UNIQUE NOT NULL," +
                    " balance DECIMAL(10, 2) NOT NULL DEFAULT 0.00," +
                    "created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (customer_id) REFERENCES customer(id));");

            this.connection.commit();
        } catch (SQLException e) {
            System.err.println("SQL exception. " + e);
            try {
                this.connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }
}
