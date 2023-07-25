package oop.course.entity.transaction;

import java.math.*;
import java.time.*;

public final class ApprovedRequest implements Transaction {
    private final String type;
    private final BigDecimal amount;
    private final LocalDateTime date;

    public ApprovedRequest(String type, BigDecimal amount, LocalDateTime date) {
        this.type = type;
        this.amount = amount;
        this.date = date;
    }

    @Override
    public String json() {
        return String.format("{%n\"type\":\"%s\",%n\"amount\":\"%s\",\"date\":\"%s\",%n}",
                this.type, this.amount, this.date);
    }

    @Override
    public BigDecimal balanceChange() {
        if ("withdraw".equals(this.type)) {
            return amount.negate();
        } else if ("deposit".equals(this.type)) {
            return amount;
        } else {
            throw new IllegalStateException("Type of a request should be either withdraw or deposit");
        }
    }
}
