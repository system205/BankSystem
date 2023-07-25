package oop.course.entity;

import oop.course.errors.exceptions.*;
import oop.course.miscellaneous.*;
import org.slf4j.*;

import java.sql.*;
import java.time.*;

public final class Offer implements JSON {
    private static final Logger log = LoggerFactory.getLogger(Offer.class);
    private final long id;
    private final Connection connection;

    public Offer(long id, Connection connection) {
        this.id = id;
        this.connection = connection;
        log.trace("Created offer with id {}", this.id);
    }

    @Override
    public String json() throws Exception {
        Details details = details();
        return String.format("{%n\"id\":\"%s\",%n%s%n}", this.id, details.json());
    }

    public void update(String status) throws Exception {
        try (
                PreparedStatement statement = this.connection.prepareStatement(
                        "UPDATE offers SET status = ? WHERE id = ?"
                );
                PreparedStatement checkStatement = this.connection.prepareStatement(
                        "SELECT status FROM offers WHERE id = ?"
                );
                PreparedStatement roleStatement = this.connection.prepareStatement(
                        """
                                INSERT INTO roles (role, customer_id)
                                VALUES
                                (
                                    'manager',
                                    (
                                        SELECT customer.id
                                        FROM customer
                                        INNER JOIN offers ON customer_email = email
                                        WHERE offers.id = ?
                                    )
                                )
                                """
                )
        ) {
            checkStatement.setLong(1, this.id);
            ResultSet check = checkStatement.executeQuery();
            check.next();
            if (!check.getString(1).equals("pending")) {
                throw new IllegalStateException("The offer is already reviewed.");
            }

            statement.setString(1, status);
            statement.setLong(2, this.id);
            statement.executeUpdate();

            if (status.equals("accepted")) {
                log.trace("Accepted state");
                roleStatement.setLong(1, this.id);
                roleStatement.executeUpdate();
            } else if (status.equals("rejected")) {
                log.trace("Rejected state");
            } else {
                throw new IllegalStateException("New status must be either accepted or rejected");
            }

            this.connection.commit();
        } catch (SQLException e) {
            log.error("Error when updating the status of an Offer");
            try {
                this.connection.rollback();
            } catch (SQLException ex) {
                throw new InternalErrorException(ex);
            }
            throw new InternalErrorException(e);
        }
    }

    private Details details() throws Exception {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "SELECT customer_email, status, created_at FROM offers WHERE id = ?;"
        )) {
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();

            if (!result.next()) throw new IllegalStateException("The offer with id " + this.id + " does not exist");

            return new Details(
                    result.getString(1),
                    result.getString(2),
                    result.getTimestamp(3).toLocalDateTime()
            );
        } catch (SQLException e) {
            throw new InternalErrorException(e);
        }
    }

    private static class Details implements JSON {
        private final String email;
        private final String status;
        private final LocalDateTime timestamp;

        private Details(String email, String status, LocalDateTime timestamp) {
            this.email = email;
            this.status = status;
            this.timestamp = timestamp;
        }


        public String json() {
            return String.format("\"customerEmail\":\"%s\",%n,\"status\":\"%s\",%n\"date\":\"%s\"",
                    this.email, this.status, this.timestamp);
        }
    }
}
