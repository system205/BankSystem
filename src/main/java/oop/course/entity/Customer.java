package oop.course.entity;

import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.*;
import oop.course.entity.account.*;
import oop.course.errors.exceptions.*;
import oop.course.miscellaneous.interfaces.*;
import org.slf4j.*;

import java.sql.*;
import java.util.Date;
import java.util.*;

public final class Customer {
    private static final Logger log = LoggerFactory.getLogger(Customer.class);
    private final String email;
    private final Connection connection;

    public Customer(Connection connection, String id) {
        this.connection = connection;
        this.email = id;
    }

    public Customer(Connection connection, Form form) throws Exception {
        this(connection, form.stringField("email"));
    }

    @Override
    public String toString() {
        return String.format("Customer with email: %s", this.email);
    }

    public void save(Form details) throws Exception {
        if (this.exists()) {
            throw new ConflictException("Email is already presented");
        }
        try (
            PreparedStatement statement = this.connection.prepareStatement(
                "INSERT INTO customer (email, name, surname, password) VALUES (?, ?, ?, ?)"
            );
            PreparedStatement roleStatement = this.connection.prepareStatement(
                """
                    INSERT INTO roles (role, customer_id)
                    VALUES ('user', (SELECT id FROM customer WHERE email = ?))
                    """
            )
        ) {
            statement.setString(1, this.email);
            statement.setString(2, details.stringField("name"));
            statement.setString(3, details.stringField("surname"));
            statement.setString(4, details.stringField("password"));
            roleStatement.setString(1, this.email);

            log.info("Add role user to a new customer");
            statement.execute();
            roleStatement.execute();

            this.connection.commit();
        } catch (SQLException e) {
            log.error("Exception when saving a customer");
            try {
                this.connection.rollback();
            } catch (SQLException ex) {
                throw new InternalErrorException(ex);
            }
            log.error("Error when saving a customer with email: {}", this.email);
            throw new InternalErrorException(e);
        }
    }

    public Account account(String id) throws Exception {
        // Check that customer owns the account and then return.
        try (
            PreparedStatement statement = this.connection.prepareStatement(
                """
                    SELECT 1 FROM customer
                    INNER JOIN checking_account on customer_id = id
                    WHERE email = ? AND account_number=?
                    """
            )
        ) {
            statement.setString(1, this.email);
            statement.setString(2, id);

            ResultSet result = statement.executeQuery();
            if (!result.next())
                throw new IllegalStateException(
                    String.format("The account with number %s is not owned by customer %s", id, this.email)
                );

            return new CheckingAccount(id, this.connection);
        } catch (SQLException e) {
            throw new InternalErrorException(e);
        }
    }

    public Collection<String> roles() throws Exception {
        log.trace("Retrieving roles from the database");

        try (
            PreparedStatement statement = this.connection.prepareStatement(
                "SELECT role FROM roles INNER JOIN customer ON id=customer_id WHERE email=?"
            )
        ) {
            statement.setString(1, this.email);
            ResultSet result = statement.executeQuery();

            Collection<String> roles = new LinkedList<>();
            while (result.next())
                roles.add(result.getString(1));

            return roles;
        } catch (SQLException e) {
            throw new InternalErrorException(e);
        }
    }

    public List<Account> accounts() throws Exception {
        log.trace("Retrieving account from the database");

        final String sql = """
            SELECT account_number FROM customer
            INNER JOIN checking_account on customer_id = id
            WHERE email = ? AND active = TRUE
            """;
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setString(1, this.email);
            ResultSet resultSet = statement.executeQuery();

            List<Account> accounts = new LinkedList<>();
            while (resultSet.next())
                accounts.add(
                    new CheckingAccount(resultSet.getString(1), this.connection)
                );

            log.info("Found {} accounts.", accounts.size());
            return accounts;
        } catch (SQLException e) {
            log.error("Error when retrieving accounts");
            throw new InternalErrorException(e);
        }
    }

    public Token token(String signingKey, String password, long duration) throws Exception {
        if (!password().equals(password))
            throw new IllegalAccessException("Illegal access");

        return new Token(
            JWT.create()
                .withSubject(this.email)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + duration))
                .sign(Algorithm.HMAC256(signingKey))
        );
    }

    private String password() throws Exception {
        try (
            PreparedStatement statement = this.connection.prepareStatement(
                "SELECT password FROM customer WHERE email = ?"
            )
        ) {
            statement.setString(1, this.email);
            ResultSet result = statement.executeQuery();

            if (result.next())
                return result.getString(1);
            else
                throw new IllegalStateException("Password of customer must be found");

        } catch (SQLException e) {
            log.error("Exception when retrieving customer's password");
            throw new InternalErrorException(e);
        }
    }

    public Offer applyForJob() throws Exception {
        log.info("Try apply for a job");
        try (
            PreparedStatement statement = this.connection.prepareStatement(
                "INSERT INTO offers (customer_email, status) VALUES (?, 'pending') RETURNING id"
            );
            PreparedStatement checkStatement = this.connection.prepareStatement(
                "SELECT 1 FROM offers WHERE customer_email = ?"
            )
        ) {
            checkStatement.setString(1, this.email);
            ResultSet check = checkStatement.executeQuery();
            if (check.next())
                throw new IllegalStateException("Bad request. Customer has already applied for a job");

            statement.setString(1, this.email);
            ResultSet result = statement.executeQuery();
            result.next();

            Offer offer = new Offer(result.getLong(1), this.connection);

            this.connection.commit();
            return offer;
        } catch (SQLException e) {
            log.error("Exception when applying for a manager job");
            try {
                this.connection.rollback();
            } catch (SQLException ex) {
                throw new InternalErrorException(ex);
            }
            throw new InternalErrorException(e);
        }
    }

    public Offer offer() throws Exception {
        try (
            PreparedStatement statement = this.connection.prepareStatement(
                "SELECT id FROM offers WHERE customer_email = ?"
            )
        ) {
            statement.setString(1, this.email);
            ResultSet result = statement.executeQuery();
            if (!result.next())
                throw new IllegalStateException("Offer of a customer is not found. Customer must exist");

            return new Offer(result.getLong(1), this.connection);
        } catch (SQLException e) {
            log.error("Error when extracting offer from the customer");
            throw new InternalErrorException(e);
        }
    }

    public boolean exists() throws Exception {
        try (
            PreparedStatement statement = this.connection.prepareStatement(
                "SELECT COUNT(*) FROM customer WHERE email = ?"
            )
        ) {
            statement.setString(1, this.email);
            ResultSet result = statement.executeQuery();
            result.next();

            return result.getInt(1) > 0;
        } catch (SQLException e) {
            log.error("Error when checking whether a customer exists");
            throw new InternalErrorException(e);
        }
    }

    public boolean correctCredentials(Form form) throws Exception {
        String password = form.stringField("password");
        boolean correct = this.exists() && password.equals(this.password());
        if (correct)
            log.debug(
                "Invalid credentials:\nEmail: {}\nPassword: {}",
                this.email,
                password
            );
        else log.debug("Customer credentials are wrong");

        return correct;
    }

    public void deleteAutopayment(long paymentId) throws Exception {
        String sql = """
            DELETE FROM autopayments a
            WHERE a.id = ? AND from_account_id IN
            (
                SELECT c.account_id FROM checking_account c
                WHERE c.customer_id =
                (
                    SELECT customer.id FROM customer WHERE email = ?
                )
            );
            """;
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setLong(1, paymentId);
            statement.setString(2, this.email);
            statement.execute();
            this.connection.commit();
        } catch (SQLException e) {
            log.error("Exception when deleting an auto-payment");
            try {
                this.connection.rollback();
            } catch (SQLException ex) {
                throw new InternalErrorException(ex);
            }
            throw new InternalErrorException(e);
        }
    }
}
