package oop.course.implementations;

import oop.course.interfaces.*;

import java.math.*;
import java.sql.*;

/**
 * A simple bank account
 */
public class CheckingAccount implements Account {
    private final String number;

    private final String table;
    private final Connection connection;

    public CheckingAccount(String id, String table, Connection connection) {
        this.number = id;
        this.table = table;
        this.connection = connection;
    }

    @Override
    public long balance() {
        try (PreparedStatement statement = this.connection.prepareStatement(
                String.format("SELECT balance from %s where account_number=?", this.table));
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

    public String json() {
        return String.format("{\"accountNumber\":\"%s\", \"balance\":\"%s\"}", this.number, balance());
    }
}
