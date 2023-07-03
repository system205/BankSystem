package oop.course.implementations;

import oop.course.interfaces.*;

import java.math.*;
import java.sql.*;

/**
 * A simple bank account
 */
public class CheckingAccount implements Account {
    private final String number;

    private final Connection connection;

    /**
     * Creates an account with the specified id
     */
    public CheckingAccount(String id, Connection connection) {
        this.number = id;
        this.connection = connection;
    }

    /**
     * Creates not saved account
     */
    public CheckingAccount(Connection connection) {
        this(generateId(), connection);
    }

    private static String generateId() {
        String id = String.valueOf(System.currentTimeMillis());
        return id.substring(id.length() - 10);
    }

    @Override
    public long balance() {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "SELECT balance from checking_account where account_number=?");
        ) {
            statement.setString(1, this.number);
            ResultSet results = statement.executeQuery();
            if (!results.next()) {
                throw new RuntimeException("The account with number: " + this.number + " was not found");
            }
            return results.getLong(1);
        } catch (SQLException e) {
            System.out.println("Exception when retrieving balance of checking account");
            throw new RuntimeException(e);
        }
    }

    @Override
    public Transaction transfer(String accountNumber, BigDecimal amount) {
        try (PreparedStatement transactionStatement = this.connection.prepareStatement(
                "INSERT INTO transactions (sender_number, receiver_number, amount) VALUES (?, ?, ?)"
        );
             PreparedStatement spendStatement = this.connection.prepareStatement(
                     "UPDATE checking_account SET balance = (SELECT balance FROM checking_account WHERE account_number = ?) - ? WHERE account_number = ?"
             );
             PreparedStatement payStatement = this.connection.prepareStatement(
                     "UPDATE checking_account SET balance = (SELECT balance FROM checking_account WHERE account_number = ?) + ? WHERE account_number = ?"
             );) {
            transactionStatement.setString(1, this.number);
            transactionStatement.setString(2, accountNumber);
            transactionStatement.setBigDecimal(3, amount);
            spendStatement.setString(1, this.number);
            payStatement.setString(1, accountNumber);
            spendStatement.setString(3, this.number);
            payStatement.setString(3, accountNumber);
            spendStatement.setBigDecimal(2, amount);
            payStatement.setBigDecimal(2, amount);
            transactionStatement.execute();
            spendStatement.execute();
            payStatement.execute();
            this.connection.commit();
        } catch (SQLException e) {
            try {
                this.connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.err.println("SQL error when transferring money. Internal error: " + e);
            throw new RuntimeException(e);
        }
        return new SimpleTransaction(this.number, accountNumber, amount);
    }

    @Override
    public String json() {
        return String.format("{\"accountNumber\":\"%s\", \"balance\":\"%s\"}", this.number, balance());
    }

    @Override
    public void save(String customerEmail) {
        try (PreparedStatement accountStatement = this.connection.prepareStatement(
                "INSERT INTO checking_account (customer_id, bank_name, account_number) VALUES (?, ?, ?)");
             PreparedStatement customerStatement = this.connection.prepareStatement(
                     "SELECT id FROM customer WHERE email=?")
        ) {
            // Identify customer in DB
            customerStatement.setString(1, customerEmail);
            ResultSet result = customerStatement.executeQuery();
            if (!result.next())
                throw new RuntimeException("Customer with the email: " + customerEmail + " was not found");
            final long id = result.getLong(1);

            // Save new checking account
            accountStatement.setLong(1, id);
            accountStatement.setString(2, "AKG");
            accountStatement.setString(3, this.number);
            accountStatement.execute();
            this.connection.commit();
        } catch (SQLException e) {
            try {
                this.connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }
}
