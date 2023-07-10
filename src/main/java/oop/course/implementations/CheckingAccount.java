package oop.course.implementations;

import oop.course.entity.*;
import oop.course.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.*;
import java.sql.*;
import java.util.*;

/**
 * A simple bank account
 */
public class CheckingAccount implements Account {
    private static final Logger log = LoggerFactory.getLogger(CheckingAccount.class);

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

    /**
     * The most important method of an Account object
     */
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
             );
             PreparedStatement enoughMoneyStatement = this.connection.prepareStatement(
                     "SELECT balance FROM checking_account WHERE account_number = ?"
             )) {
            enoughMoneyStatement.setString(1, this.number);
            ResultSet result = enoughMoneyStatement.executeQuery();
            result.next();
            final BigDecimal balance = result.getBigDecimal(1);
            if (balance.compareTo(amount) < 0){
                throw new IllegalArgumentException("Not enough money to perform a transaction");
            }
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
            log.error("SQL error when transferring money. Internal error: " + e);
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

    @Override
    public CustomerRequest attachRequest(String type, BigDecimal amount) {
        String sql = "INSERT INTO requests (account_number, amount, type, status) VALUES (?, ?, ?, 'pending') RETURNING id";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setString(1, this.number);
            statement.setBigDecimal(2, amount);
            if (!(type.equals("withdraw") || type.equals("deposit"))) {
                throw new RuntimeException("Bad request. Type should be withdraw or deposit");
            }
            statement.setString(3, type);
            ResultSet result = statement.executeQuery();
            result.next();
            long id = result.getLong(1);
            this.connection.commit();
            return new CustomerRequest(
                    id,
                    this.connection
            );
        } catch (SQLException e) {
            try {
                this.connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<CustomerRequest> requests() {
        String sql = "SELECT id FROM requests WHERE account_number = ?";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setString(1, this.number);
            ResultSet result = statement.executeQuery();
            List<CustomerRequest> requests = new LinkedList<>();
            while (result.next()) {
                requests.add(
                        new CustomerRequest(
                                result.getLong(1),
                                this.connection
                        )
                );
            }
            return requests;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deposit(BigDecimal amount) {
        withdraw(amount.negate());
    }

    @Override
    public void withdraw(BigDecimal amount) {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "UPDATE checking_account SET balance = (SELECT balance FROM checking_account WHERE account_number = ?) - ? WHERE account_number = ?"
        )) {
            statement.setString(1, this.number);
            statement.setBigDecimal(2, amount);
            statement.setString(3, this.number);
            statement.execute();
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
