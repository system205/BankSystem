package oop.course.entity;

import oop.course.tools.*;
import org.slf4j.*;

import java.sql.*;

public class Offer implements JSON {
    private static final Logger log = LoggerFactory.getLogger(Offer.class);
    private final long id;
    private final Connection connection;

    public Offer(long id, Connection connection) {
        this.id = id;
        this.connection = connection;
        log.trace("Created offer with id {}", this.id);
    }

    @Override
    public String json() {
        // TODO return details such as status and customer email
        return String.format("{%n\"id\":\"%s\"%n}", this.id);
    }

    public void update(String status) {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "UPDATE offers SET status = ? WHERE id = ?"
        ); PreparedStatement checkStatement = this.connection.prepareStatement(
                "SELECT status FROM offers WHERE id = ?"
        ); PreparedStatement roleStatement = this.connection.prepareStatement(
                "INSERT INTO roles (role, customer_id) VALUES ('manager', " +
                        "(SELECT customer.id FROM customer INNER JOIN offers ON customer_email=email WHERE offers.id = ?))"
        )) {
            checkStatement.setLong(1, this.id);
            ResultSet check = checkStatement.executeQuery();
            check.next();
            if (!check.getString(1).equals("pending")) {
                throw new RuntimeException("The offer is already reviewed.");
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
            } else
                throw new IllegalStateException("New status must be either accepted or rejected");

            this.connection.commit();
            log.info("The status of offer {} is now rejected", this.id);
        } catch (SQLException e) {
            log.error("Error when updating the status of an Offer", e);
            try {
                this.connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }
}
