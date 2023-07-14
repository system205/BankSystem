package oop.course.entity;

import org.slf4j.*;

import java.sql.*;
import java.util.*;

public class Admin {
    private static final Logger log = LoggerFactory.getLogger(Admin.class);
    private final Connection connection;

    public Admin(Connection connection) {
        this.connection = connection;
        log.trace("Admin is created");
    }

    public List<Offer> offers() {
        log.info("Retrieving all offers from the database");
        try (PreparedStatement statement = this.connection.prepareStatement(
                "SELECT id FROM offers WHERE status = 'pending';"
        )) {
            ResultSet result = statement.executeQuery();
            List<Offer> offers = new LinkedList<>();
            while (result.next()) {
                offers.add(new Offer(result.getLong(1), this.connection));
            }
            log.info("Found {} offers", offers.size());
            return offers;
        } catch (SQLException e) {
            log.error("Error when retrieving offers from a database", e);
            throw new RuntimeException(e);
        }
    }

    public List<AutoPayment> payments() {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "SELECT id FROM autopayments;"
        )) {
            ResultSet result = statement.executeQuery();
            List<AutoPayment> payments = new LinkedList<>();
            while (result.next()) {
                payments.add(new AutoPayment(result.getLong(1), this.connection));
            }
            log.info("Found {} autopayments", payments.size());
            return payments;
        } catch (SQLException e) {
            log.error("Error when retrieving offers from a database", e);
            throw new RuntimeException(e);
        }
    }
}
