package oop.course.entity.transaction;

import java.math.*;
import java.time.*;

public final class CustomerTransaction implements Transaction {
    private final LocalDateTime dateTime;
    private final BigDecimal amount;
    private final String type;
    private final String anotherNumber;

    public CustomerTransaction(LocalDateTime dateTime, BigDecimal amount, String anotherNumber, String type) {
        this.dateTime = dateTime;
        this.amount = amount;
        this.type = type;
        this.anotherNumber = anotherNumber;
    }

    @Override
    public String json() {
        if ("income".equals(type)) {
            return String.format("{%n\"type\":\"%s\",%n\"from\":\"%s\",%n\"amount\":\"%s\",%n\"date\":\"%s\"%n}",
                this.type, this.anotherNumber, this.amount, this.dateTime);
        } else if ("outcome".equals(type)) {
            return String.format("{%n\"type\":\"%s\",%n\"from\":\"%s\",%n\"amount\":\"%s\",%n\"date\":\"%s\"%n}",
                this.type, this.anotherNumber, this.amount, this.dateTime);
        } else throw new IllegalStateException("Type of a customer transaction must be either income or outcome");
    }

    @Override
    public BigDecimal balanceChange() {
        if ("income".equals(this.type))
            return this.amount;
        else if ("outcome".equals(this.type))
            return this.amount.negate();
        else throw new IllegalStateException("The type of a customer transaction should be either income or outcome");
    }
}
