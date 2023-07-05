package oop.course.entity;

import oop.course.tools.*;
import org.slf4j.*;

import java.sql.*;

public class CustomerRequest implements JSON {
    private static final Logger log = LoggerFactory.getLogger(CustomerRequest.class);
    private final long id;
    private final Connection connection;

    public CustomerRequest(long id, Connection connection) {
        this.id = id;
        this.connection = connection;
        log.debug("Created new customer request");
    }

    @Override
    public String json() {
        RequestDetails details = details();
        return String.format("{%n\"accountNumber\":\"%s\",%n\"amount\":\"%s\",%n\"type\":\"%s\"%n}",
                details.accountNumber, details.amount, details.type);
    }

    private RequestDetails details() {
        String sql = "SELECT account_number, amount, type FROM requests WHERE id = ?";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setLong(1, this.id);
            ResultSet result = statement.executeQuery();
            result.next();
            return new RequestDetails(
                    result.getString(1),
                    result.getDouble(2),
                    result.getString(3)
            );
        } catch (SQLException e) {
            log.error("Error when retrieving a request details", e);
            throw new RuntimeException(e);
        }
    }


    private static class RequestDetails {
        private static final Logger log = LoggerFactory.getLogger(RequestDetails.class);
        private final String accountNumber;
        private final double amount;
        private final String type;

        private RequestDetails(String accountNumber, double amount, String type) {
            this.accountNumber = accountNumber;
            this.amount = amount;
            this.type = type;
            log.trace("Params: {}, {}, {}", accountNumber, amount, type);
        }
    }
}
