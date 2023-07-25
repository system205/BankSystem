package oop.course.entity;


import oop.course.errors.exceptions.*;
import oop.course.storage.migrations.*;
import org.slf4j.*;

import java.sql.*;
import java.util.*;

public final class Admin implements Initializer {
    private static final Logger log = LoggerFactory.getLogger(Admin.class);
    private final Connection connection;

    public Admin(Connection connection) {
        this.connection = connection;
        log.trace("Admin is created");
    }

    public List<Offer> offers() throws Exception {
        log.info("Retrieving all offers from the database");
        try (
            PreparedStatement statement = this.connection.prepareStatement(
                "SELECT id FROM offers WHERE status = 'pending';"
            )
        ) {
            ResultSet result = statement.executeQuery();

            List<Offer> offers = new LinkedList<>();
            while (result.next())
                offers.add(new Offer(result.getLong(1), this.connection));
            log.info("Found {} offers", offers.size());

            return offers;
        } catch (SQLException e) {
            log.error("Error when retrieving offers from a database");
            throw new InternalErrorException(e);
        }
    }

    private List<AutoPayment> payments() throws InternalErrorException {
        try (
            PreparedStatement statement = this.connection.prepareStatement(
                "SELECT id FROM autopayments;"
            )
        ) {
            ResultSet result = statement.executeQuery();

            List<AutoPayment> payments = new LinkedList<>();
            while (result.next())
                payments.add(new AutoPayment(result.getLong(1), this.connection));
            log.debug("Found {} autopayments", payments.size());

            return payments;
        } catch (SQLException e) {
            log.error("Error when retrieving offers from a database");
            throw new InternalErrorException(e);
        }
    }

    @Override
    public void init() {
        log.debug("Start initializing admin");
        // Create or check that admin exists
        try (
            PreparedStatement statement = this.connection.prepareStatement(
                "SELECT * FROM customer WHERE email = 'admin';"
            );
            PreparedStatement createStatement = this.connection.prepareStatement(
                """
                    INSERT INTO customer (email, name, surname, password)
                    VALUES ('admin', 'admin', 'admin', 'admin')
                    RETURNING id;
                    """
            );
            PreparedStatement roleStatement = this.connection.prepareStatement(
                "INSERT INTO roles (role, customer_id) VALUES ('admin', ?);"
            )
        ) {
            ResultSet result = statement.executeQuery();
            if (!result.next()) { // create admin
                log.debug("Start creating new Admin");
                ResultSet id = createStatement.executeQuery();
                if (id.next()) {
                    roleStatement.setLong(1, id.getLong(1));
                    roleStatement.execute();
                    log.debug("Role is added to admin");
                }
                log.info("Admin is created");
            } else
                log.info("Admin is already initialized");
        } catch (SQLException e) {
            log.error("Admin was not initialized");
            try {
                this.connection.rollback();
            } catch (SQLException ex) {
                log.error("Failed to rollback db changes. Closing..");
                throw new Error(ex);
            }
            throw new Error(e);
        }

        // Resume autopayments
        List<AutoPayment> list;
        try {
            list = payments();
        } catch (InternalErrorException e) {
            throw new Error("Autopayments can't be resumed");
        }

        list.forEach(AutoPayment::pay);
        log.info("{} autopayments are resumed", list.size());
    }
}
