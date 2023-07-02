package oop.course.entity;

import oop.course.implementations.*;
import oop.course.interfaces.*;
import oop.course.tools.interfaces.*;

import java.sql.*;

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
        try (PreparedStatement statement = this.connection.prepareStatement(
                "SELECT 1 FROM customer LEFT JOIN checking_account on customer_id = id WHERE email = ?"
        )) {
            statement.setString(1, this.email);
            if (!statement.execute()) {
                throw new RuntimeException(
                        String.format("The account with number %s is not owned by customer %s", id, this.email)
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new CheckingAccount(id, "checking_account", this.connection);
    }

//    public Customer read(Long id) {
//        try (ResultSet result = new PreparedStatementWithId(connection, "SELECT * FROM customer WHERE id=?;", id).execute()) {
//            result.next();
//            return new Customer(
//                    result.getString(2),
//                    result.getString(3),
//                    result.getString(4),
//                    result.getString(4));
//        } catch (PSQLException e) {
//            System.out.println("PSQLException occurred. Maybe the customer was not found in DB. Error: " + e);
//        } catch (SQLException e) {
//            System.out.println("Some sql exception occurred");
//            throw new RuntimeException(e);
//        }
//        throw new RuntimeException("Customer with id " + id + " was not found.");
//    }
}
