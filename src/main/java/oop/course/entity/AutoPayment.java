package oop.course.entity;

import oop.course.implementations.*;
import oop.course.interfaces.*;
import oop.course.tools.*;

import java.math.*;
import java.sql.*;
import java.sql.Date;
import java.time.*;
import java.time.temporal.*;
import java.util.concurrent.*;

public class AutoPayment implements JSON {
    private final long id;
    private final Connection connection;
    private final ScheduledExecutorService timer;

    public AutoPayment(long id, Connection connection) {
        this.id = id;
        this.connection = connection;
        this.timer = Executors.newSingleThreadScheduledExecutor();
    }

    public void pay() {
        PaymentDetails details = details();
        final ScheduledFuture<?>[] task = new ScheduledFuture<?>[1];
        task[0] = this.timer.scheduleAtFixedRate(
                () -> {
                    if (!active())
                        task[0].cancel(true);

                    details.sender.transfer(details.receiverNumber, details.amount);
                }, calculateInitDelay(details.startDate.toLocalDate().atStartOfDay(),
                        details.period), details.period, TimeUnit.SECONDS
        );
    }

    private boolean active() {
        try (PreparedStatement statement = this.connection.prepareStatement("SELECT 1 FROM autopayments WHERE id = ?;")) {
            statement.setLong(1, this.id);
            ResultSet result = statement.executeQuery();
            return result.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private PaymentDetails details() {
        String sql = "SELECT ca.account_number, c.account_number, amount, start_date, period_in_seconds FROM autopayments  " +
                "INNER JOIN checking_account ca on ca.account_id = autopayments.from_account_id " +
                "INNER JOIN checking_account c ON c.account_id = autopayments.to_account_id WHERE autopayments.id = ?";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setLong(1, this.id);
            ResultSet result = statement.executeQuery();
            if (!result.next()) {
                throw new RuntimeException("The autopayment with id " + this.id + " does not exist");
            }
            return new PaymentDetails(new CheckingAccount(result.getString(1), this.connection),
                    result.getString(2),
                    result.getBigDecimal(3),
                    result.getDate(4),
                    result.getLong(5));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String json() {
        return String.format("{%n\"id\":\"%s\"%n}", this.id);
    }

    private static class PaymentDetails {
        private final Account sender;
        private final String receiverNumber;
        private final BigDecimal amount;
        private final Date startDate;
        private final long period;

        private PaymentDetails(Account sender, String receiverNumber, BigDecimal amount, Date startDate, long period) {
            this.sender = sender;
            this.receiverNumber = receiverNumber;
            this.amount = amount;
            this.startDate = startDate;
            this.period = period;
        }
    }

    private static long calculateInitDelay(LocalDateTime startDate, long period) {
        LocalDateTime now = LocalDateTime.now();
        if (startDate.isBefore(now)) {
            long periodsElapsed = startDate.until(now, ChronoUnit.SECONDS) / period;
            LocalDateTime nextPoint = startDate.plus((periodsElapsed + 1) * period, ChronoUnit.SECONDS);
            return now.until(nextPoint, ChronoUnit.SECONDS);
        } else {
            return now.until(startDate, ChronoUnit.SECONDS);
        }
    }
}
