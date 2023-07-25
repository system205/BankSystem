package oop.course.entity;

import oop.course.entity.account.Account;
import oop.course.entity.account.CheckingAccount;
import oop.course.errors.exceptions.InternalErrorException;
import oop.course.miscellaneous.*;
import org.slf4j.*;

import java.math.*;
import java.sql.*;

public class CustomerRequest implements JSON {
    private static final Logger log = LoggerFactory.getLogger(CustomerRequest.class);
    private final long id;
    private final Connection connection;

    public CustomerRequest(long id, Connection connection) {
        this.id = id;
        this.connection = connection;
        log.trace("Created new customer request");
    }

    @Override
    public String json() throws Exception {
        RequestDetails details = details();
        return String.format("{%n\"id\"             :\"%s\",%n" +
                        "\"accountNumber\"  :\"%s\",%n" +
                        "\"amount\"         :\"%s\",%n" +
                        "\"type\"           :\"%s\",%n" +
                        "\"status\"         :\"%s\" %n}",
                this.id, details.accountNumber, details.amount, details.type, details.status);
    }

    public void update(String status) throws Exception {
        RequestDetails details = details();
        Account account = new CheckingAccount(
                details.accountNumber,
                this.connection
        );

        if (!details.status.equals("pending"))
            throw new IllegalStateException("Only pending requests must be considered");

        if ("approved".equals(status)) {
            log.info("The customer's request {} is approved. Start {}ing", this.id, details.type);

            if ("deposit".equals(details.type)) {
                account.deposit(details.amount);
            } else if ("withdraw".equals(details.type))
                account.withdraw(details.amount);
            else throw new IllegalStateException("type of a request can't be anything but withdraw or deposit");
        } else if ("denied".equals(status)) {
            log.info("The customer's request {} is denied", this.id);
        } else {
            log.error("New status of a request must be approved or denied.");
            throw new IllegalStateException("Bad Request. Type must be either approved or denied");
        }

        updateStatus(status);
    }

    private RequestDetails details() throws Exception {
        String sql = "SELECT account_number, amount, type, status FROM requests WHERE id = ?";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setLong(1, this.id);
            ResultSet result = statement.executeQuery();

            if (!result.next()) {
                log.debug("Requests details with id {} was not found.", this.id);
                throw new IllegalStateException("Not found details of request by id");
            }

            return new RequestDetails(
                    result.getString(1),
                    result.getBigDecimal(2),
                    result.getString(3),
                    result.getString(4)
            );
        } catch (SQLException e) {
            log.error("Error when retrieving a request details");
            throw new InternalErrorException(e);
        }
    }

    private void updateStatus(String status) throws Exception {
        String sql = "UPDATE requests SET status = ? WHERE id = ?";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setLong(2, this.id);
            statement.execute();
            this.connection.commit();
        } catch (SQLException e) {
            log.error("Error when updating the status of a customer's request");
            try {
                this.connection.rollback();
            } catch (SQLException ex) {
                throw new InternalErrorException(ex);
            }
            throw new InternalErrorException(e);
        }
    }

    private static class RequestDetails {
        private static final Logger log = LoggerFactory.getLogger(RequestDetails.class);
        private final String accountNumber;
        private final BigDecimal amount;
        private final String type;
        private final String status;

        private RequestDetails(String accountNumber, BigDecimal amount, String type, String status) {
            this.accountNumber = accountNumber;
            this.amount = amount;
            this.type = type;
            this.status = status;
            log.trace("Params: {}, {}, {}, {}", accountNumber, amount, type, status);
        }

        @Override
        public String toString() {
            return "{" +
                    "accountNumber='" + accountNumber + '\'' +
                    ", amount=" + amount +
                    ", type='" + type + '\'' +
                    ", status='" + status + '\'' +
                    '}';
        }
    }
}
