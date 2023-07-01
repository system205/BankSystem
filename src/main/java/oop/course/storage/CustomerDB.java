package oop.course.storage;

import oop.course.entity.*;
import org.postgresql.util.*;

import java.sql.*;

public class CustomerDB implements Database<Long, Customer> {
    private final Connection connection;
    private final String table;

    public CustomerDB(Connection connection, String table) {
        this.connection = connection;
        this.table = table;
    }

    @Override
    public Customer read(Long id) {
        try (ResultSet result = new PreparedStatementWithId(connection, "SELECT * FROM customer WHERE id=?;", id).execute()) {
            result.next();
            return new Customer(
                    result.getString(2),
                    result.getString(3),
                    result.getString(4),
                    result.getString(4));
        } catch (PSQLException e) {
            System.out.println("PSQLException occurred. Maybe the customer was not found in DB. Error: " + e);
        } catch (SQLException e) {
            System.out.println("Some sql exception occurred");
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Customer with id " + id + " was not found.");
    }

    @Override
    public void write(Customer customer) {
        // How to get data from customer?
        try (Statement statement = connection.createStatement()) {
            statement.execute(customer.toSqlInsert(this.table));
            this.connection.commit();
        } catch (SQLException e) {
            try {
                this.connection.rollback();
            } catch (SQLException ex) {
                System.out.println("Failed to rollback");
                throw new RuntimeException(ex);
            }
            System.err.println("Failed to insert a customer into database");
            throw new RuntimeException(e);
        }
    }
}
