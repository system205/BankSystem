package oop.course.storage;

import oop.course.entity.*;
import org.postgresql.util.*;

import java.sql.*;
import java.util.*;

public class CustomerDB implements Entity<Customer> {
    private final Connection connection;

    public CustomerDB(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Customer> read(long id) {
        try (ResultSet result = new PreparedStatementWithId(connection, "SELECT * FROM customer WHERE id=?;", id).execute()) {
            result.next();
            Customer customer = new Customer(
                    result.getString(2),
                    result.getString(3),
                    result.getString(4),
                    result.getString(4));
            return Optional.of(customer);
        } catch (PSQLException e) {
            System.out.println("PSQLException occurred. Maybe the customer was not found in DB");
        } catch (SQLException e) {
            System.out.println("Some sql exception occurred");
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public void write(Customer object) {
        // How to get data from customer?
    }
}
