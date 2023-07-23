package oop.course.implementations;

import oop.course.entity.*;
import oop.course.exceptions.*;
import oop.course.interfaces.*;
import org.slf4j.*;
import oop.course.tools.interfaces.*;

import java.math.*;
import java.sql.*;
import java.sql.Date;
import java.time.*;
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

    private long balance() throws Exception {
        try (
                PreparedStatement statement = this.connection.prepareStatement(
                        "SELECT balance from checking_account where account_number=?"
                )
        ) {
            statement.setString(1, this.number);
            ResultSet results = statement.executeQuery();
            if (!results.next()) {
                throw new AccountException("The account with number: " + this.number + " was not found");
            }
            return results.getLong(1);
        } catch (SQLException e) {
            System.out.println("Exception when retrieving balance of checking account");
            throw new InternalErrorException(e);
        }
    }

    /**
     * The most important method of an Account object
     */
    @Override
    public Transaction transfer(String accountNumber, BigDecimal amount) throws Exception {
        if (!isActive()) throw new IllegalStateException("Can't transfer from inactive account");
        try (
                PreparedStatement transactionStatement = this.connection.prepareStatement(
                        "INSERT INTO transactions (sender_number, receiver_number, amount) VALUES (?, ?, ?)"
                );
                PreparedStatement spendStatement = this.connection.prepareStatement(
                        """
                                UPDATE checking_account
                                SET balance = (SELECT balance FROM checking_account WHERE account_number = ?) - ?
                                WHERE account_number = ?
                                """
                );
                PreparedStatement payStatement = this.connection.prepareStatement(
                        """
                                UPDATE checking_account
                                SET balance = (SELECT balance FROM checking_account WHERE account_number = ?) + ?
                                WHERE account_number = ?
                                """
                );
                PreparedStatement enoughMoneyStatement = this.connection.prepareStatement(
                        "SELECT balance FROM checking_account WHERE account_number = ?"
                );
                PreparedStatement receiverExists = this.connection.prepareStatement(
                        "SELECT 1 FROM checking_account WHERE account_number = ?"
                )
        ) {
            enoughMoneyStatement.setString(1, this.number);
            ResultSet result = enoughMoneyStatement.executeQuery();
            if (!result.next()) {
                throw new AccountException("The account with number: " + this.number + " was not found");
            }
            receiverExists.setString(1, accountNumber);
            ResultSet receiverExistsResult = receiverExists.executeQuery();
            if (!receiverExistsResult.next()) {
                throw new AccountException("The account with number: " + accountNumber + " was not found");
            }
            final BigDecimal balance = result.getBigDecimal(1);
            if (balance.compareTo(amount) < 0) {
                throw new AccountException("Not enough money to perform a transaction");
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
                throw new InternalErrorException(ex);
            }
            log.error("SQL error when transferring money. Internal error: " + e);
            throw new InternalErrorException(e);
        }
        return new SimpleTransaction(this.number, accountNumber, amount);
    }

    @Override
    public String json() throws Exception {
        if (!isActive()) throw new IllegalStateException("Can't see the inactive account");
        return String.format("{\"accountNumber\":\"%s\", \"balance\":\"%s\"}", this.number, balance());
    }

    @Override
    public void save(String customerEmail) throws Exception {
        // TODO - could be simplified
        try (
                PreparedStatement accountStatement = this.connection.prepareStatement(
                        "INSERT INTO checking_account (customer_id, bank_name, account_number) VALUES (?, ?, ?)"
                );
                PreparedStatement accountExistsStatement = this.connection.prepareStatement(
                        "SELECT 1 FROM checking_account WHERE account_number = ?"
                );
                PreparedStatement customerStatement = this.connection.prepareStatement(
                        "SELECT id FROM customer WHERE email=?"
                );
                PreparedStatement customerAccountsAmountStatement = this.connection.prepareStatement(
                        "SELECT COUNT(*) FROM checking_account WHERE customer_id = ?"
                )
        ) {
            // Identify customer in DB
            customerStatement.setString(1, customerEmail);
            ResultSet result = customerStatement.executeQuery();
            if (!result.next()) {
                throw new AccountException("Customer with the email: " + customerEmail + " was not found");
            }
            accountExistsStatement.setString(1, this.number);
            ResultSet accountExistsResult = accountExistsStatement.executeQuery();
            if (accountExistsResult.next()) {
                log.debug("Account with number: " + this.number + " is already saved in DB");
                return;
            }

            final long id = result.getLong(1);
            customerAccountsAmountStatement.setLong(1, id);
            ResultSet customerAccountsAmountResult = customerAccountsAmountStatement.executeQuery();
            customerAccountsAmountResult.next();
            final int numberOfAccounts = customerAccountsAmountResult.getInt(1);
            log.debug("Number of accounts: " + numberOfAccounts);
            if (numberOfAccounts >= 5) {
                throw new ConflictException("Customer can't have more than 5 accounts");
            }
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
                throw new InternalErrorException(ex);
            }
            throw new InternalErrorException(e);
        }
    }

    @Override
    public CustomerRequest attachRequest(String type, BigDecimal amount) throws Exception {
        if (!isActive()) throw new IllegalStateException("Can't put requests to inactive account");
        String sql = """
                INSERT INTO requests (account_number, amount, type, status)
                VALUES (?, ?, ?, 'pending') RETURNING id
                """;
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setString(1, this.number);
            statement.setBigDecimal(2, amount);
            if (!(type.equals("withdraw") || type.equals("deposit"))) {
                throw new AccountException("Bad request. Type should be withdraw or deposit");
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
                throw new InternalErrorException(ex);
            }
            throw new InternalErrorException(e);
        }
    }

    @Override
    public Collection<CustomerRequest> requests() {
        if (!isActive()) throw new IllegalStateException("Can't see requests of inactive account");
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
    public void deposit(BigDecimal amount) throws Exception {
        if (!isActive()) throw new IllegalStateException("Can't deposit on inactive account");
        withdraw(amount.negate());
    }

    @Override
    public void withdraw(BigDecimal amount) throws Exception {
        if (!isActive()) throw new IllegalStateException("Can't withdraw from inactive account");
        try (
                PreparedStatement statement = this.connection.prepareStatement(
                        """
                                    UPDATE checking_account
                                    SET balance = (SELECT balance FROM checking_account WHERE account_number = ?) - ?
                                    WHERE account_number = ?
                                """
                )
        ) {
            statement.setString(1, this.number);
            statement.setBigDecimal(2, amount);
            statement.setString(3, this.number);
            statement.execute();
            this.connection.commit();
        } catch (SQLException e) {
            try {
                this.connection.rollback();
            } catch (SQLException ex) {
                throw new InternalErrorException(ex);
            }
            throw new InternalErrorException(e);
        }
    }

    @Override
    public List<Transaction> transactions() throws Exception {
        return transactions(
                Timestamp.from(Instant.ofEpochSecond(0)),
                Timestamp.from(Instant.ofEpochSecond(10000000000L))
        );
    }

    private List<Transaction> transactions(Timestamp start, Timestamp end) throws Exception {
        if (!isActive()) throw new IllegalStateException("Can't see transactions of inactive account");
        try (
                PreparedStatement outcomeStatement = this.connection.prepareStatement(
                        """
                                SELECT amount, created_at, receiver_number FROM transactions
                                WHERE sender_number = ? AND created_at BETWEEN ? AND ?
                                """
                );
                PreparedStatement incomeStatement = this.connection.prepareStatement(
                        """
                                SELECT amount, created_at, sender_number FROM transactions
                                WHERE receiver_number = ? AND created_at BETWEEN ? AND ?
                                """
                );
                PreparedStatement requestsStatement = this.connection.prepareStatement(
                        """
                                SELECT type, amount, created_at FROM requests
                                WHERE account_number = ? AND status = 'approved' AND created_at BETWEEN ? AND ?
                                """
                )
        ) {
            incomeStatement.setString(1, this.number);
            outcomeStatement.setString(1, this.number);
            requestsStatement.setString(1, this.number);

            incomeStatement.setTimestamp(2, start);
            incomeStatement.setTimestamp(3, end);
            outcomeStatement.setTimestamp(2, start);
            outcomeStatement.setTimestamp(3, end);
            requestsStatement.setTimestamp(2, start);
            requestsStatement.setTimestamp(3, end);

            ResultSet income = incomeStatement.executeQuery();
            ResultSet outcome = outcomeStatement.executeQuery();
            ResultSet approvedRequests = requestsStatement.executeQuery();

            List<Transaction> transactions = new ArrayList<>();
            while (income.next()) {
                transactions.add(
                        new CustomerTransaction(
                                income.getTimestamp(2).toLocalDateTime(),
                                income.getBigDecimal(1),
                                income.getString(3),
                                "income"
                        )
                );
            }
            while (outcome.next()) {
                transactions.add(
                        new CustomerTransaction(
                                outcome.getTimestamp(2).toLocalDateTime(),
                                outcome.getBigDecimal(1),
                                outcome.getString(3),
                                "outcome"
                        )
                );
            }
            while (approvedRequests.next()) {
                transactions.add(
                        new ApprovedRequest(
                                approvedRequests.getString(1),
                                approvedRequests.getBigDecimal(2),
                                approvedRequests.getTimestamp(3).toLocalDateTime()
                        )
                );
            }
            return transactions;
        } catch (SQLException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public TransactionStatement compose(LocalDate start, LocalDate end) throws Exception {
        if (!isActive()) throw new IllegalStateException("Can't see the inactive account");
        final Timestamp startTimestamp = Timestamp.valueOf(start.atStartOfDay());
        final Timestamp endTimestamp = Timestamp.valueOf(end.atStartOfDay());

        final List<Transaction> transactions = transactions(startTimestamp, endTimestamp);
        final List<Transaction> previousTransactions = transactions(
                Timestamp.from(Instant.ofEpochSecond(1)), startTimestamp
        );

        final BigDecimal startingBalance =
                previousTransactions
                        .stream()
                        .map(Transaction::balanceChange)
                        .reduce(BigDecimal::add)
                        .orElseGet(() -> new BigDecimal(0));
        final BigDecimal endingBalance =
                transactions
                        .stream()
                        .map(Transaction::balanceChange)
                        .reduce(BigDecimal::add)
                        .orElseGet(() -> new BigDecimal(0))
                        .add(startingBalance);

        // return new TransactionStatement(this.number, start, end, previousTransactions, transactions); // TODO
        return new TransactionStatement(this.number, start, end, transactions, startingBalance, endingBalance);
    }

    @Override
    public void deactivate() {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE checking_account SET active = false WHERE account_number = ?"
                )
        ) {
            statement.setString(1, this.number);
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

    @Override
    public AutoPayment createPayment(Form form) throws Exception {
        if (!isActive()) throw new IllegalStateException("Can't pay from inactive account");
        String sql = """
                INSERT INTO autopayments (from_account_id, to_account_id, amount, start_date, period_in_seconds)
                VALUES (
                    (SELECT account_id FROM checking_account account WHERE account_number = ?),
                    (SELECT account_id FROM checking_account account WHERE account_number = ?),
                    ?, ?, ?
                )
                RETURNING id;
                """;
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setString(1, this.number);
            statement.setString(2, form.stringField("receiverNumber"));
            statement.setBigDecimal(3, form.bigDecimalField("amount"));
            statement.setDate(4, Date.valueOf(form.stringField("startDate")));
            statement.setLong(5, form.longField("period"));
            ResultSet id = statement.executeQuery();
            if (!id.next()) {
                throw new IllegalStateException("Exception when inserting into autopayments");
            }
            this.connection.commit();
            return new AutoPayment(id.getLong(1), this.connection);
        } catch (SQLException e) {
            try {
                this.connection.rollback();
            } catch (SQLException ex) {
                throw new InternalErrorException(ex);
            }
            throw new InternalErrorException(e);
        }
    }

    @Override
    public List<AutoPayment> autopayments() throws Exception {
        if (!isActive()) throw new InternalErrorException("Can't see autopayments of inactive account");
        String sql = """
                SELECT id FROM autopayments
                WHERE from_account_id = (SELECT account_id FROM checking_account WHERE account_number = ?)
                """;
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setString(1, this.number);
            ResultSet result = statement.executeQuery();
            List<AutoPayment> payments = new LinkedList<>();
            while (result.next()) {
                payments.add(
                        new AutoPayment(
                                result.getLong(1),
                                this.connection
                        )
                );
            }
            return payments;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isActive() {
        try (
                PreparedStatement statement = this.connection.prepareStatement(
                        "SELECT active FROM checking_account WHERE account_number = ?"
                )
        ) {
            statement.setString(1, this.number);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getBoolean(1);
            } else {
                throw new IllegalStateException("The account with number " + this.number + " does not exist");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
