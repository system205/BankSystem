package oop.course.entity;

import oop.course.entity.transaction.*;
import oop.course.miscellaneous.*;
import org.slf4j.*;

import java.math.*;
import java.time.*;
import java.util.*;

public class TransactionStatement implements JSON {
    private static final Logger log = LoggerFactory.getLogger(TransactionStatement.class);
    private final String accountNumber;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final List<Transaction> transactions;
    private final BigDecimal startingBalance;
    private final BigDecimal endingBalance;

    public TransactionStatement(String accountNumber, LocalDate startDate, LocalDate endDate, List<Transaction> transactions, BigDecimal startingBalance, BigDecimal endingBalance) {
        this.accountNumber = accountNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.transactions = transactions;
        this.startingBalance = startingBalance;
        this.endingBalance = endingBalance;
        log.trace("Transaction statement with params: number: {}, from: {}, to: {}",
                accountNumber, startDate, endDate);
    }


    @Override
    public String json() throws Exception {
        ArrayList<String> jsonsAsString = new ArrayList<>();
        for (JSON json : this.transactions) {
            jsonsAsString.add(json.json());
        }
        String body = "[\n" + String.join(",\n", jsonsAsString) + "\n]";
        return String.format("{%n\"accountNumber\":\"%s\",%n" +
                        "\"from\":\"%s\",%n" +
                        "\"to\":\"%s\",%n" +
                        "\"startingBalance\":\"%s\",%n" +
                        "\"endingBalance\":\"%s\",%n" +
                        "\"transactions\":\"%s\"%n}", this.accountNumber, this.startDate, this.endDate,
                this.startingBalance, this.endingBalance,
                body);
    }
}
