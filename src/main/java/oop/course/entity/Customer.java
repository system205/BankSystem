package oop.course.entity;

import oop.course.implementations.*;
import oop.course.interfaces.*;
import oop.course.tools.interfaces.*;

import java.sql.*;
import java.util.*;

public class Customer {
    private final String email;
    private final Connection connection;

    public Customer(Connection connection, String id) {
        this.connection = connection;
        this.email = id;
    }

    @Override
    public String toString() {
        return String.format("Customer with email: %s", this.email);
    }

    public void save(Form details) {
        try (PreparedStatement statement = this.connection
                .prepareStatement("INSERT INTO customer (email, name, surname, password) VALUES (?, ?, ?, ?)")) {
            statement.setString(1, this.email);
            statement.setString(2, details.stringField("name"));
            statement.setString(3, details.stringField("surname"));
            statement.setString(4, details.stringField("password"));
            statement.execute();
            this.connection.commit();
        } catch (SQLException e) {
            try {
                this.connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.err.println("Error when saving a customer");
            throw new RuntimeException(e);
        }
    }

    public Account account(String id) {
        // Check that customer ownes the account and then return.
        try (PreparedStatement statement = this.connection.prepareStatement(
                "SELECT 1 FROM customer INNER JOIN checking_account on customer_id = id WHERE email = ? AND account_number=?"
        )) {
            statement.setString(1, this.email);
            statement.setString(2, id);
            ResultSet result = statement.executeQuery();
            if (!result.next()) {
                throw new RuntimeException(
                        String.format("The account with number %s is not owned by customer %s", id, this.email)
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new CheckingAccount(id, this.connection);
    }

    public Collection<String> roles() {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "SELECT role FROM roles INNER JOIN customer ON id=customer_id WHERE email=?"
        )) {
            statement.setString(1, this.email);
            ResultSet result = statement.executeQuery();
            Collection<String> roles = new LinkedList<>();
            while (result.next()) {
                roles.add(result.getString(1));
            }
            return roles;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
