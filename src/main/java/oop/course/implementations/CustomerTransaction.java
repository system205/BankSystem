package oop.course.implementations;

import oop.course.interfaces.*;

import java.math.*;
import java.time.*;

public class CustomerTransaction implements Transaction {
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
            return String.format("{\"type\":\"%s\",%n\"from\":\"%s\",%n\"amount\":\"%s\",%n\"date\":\"%s\"}",
                    this.type, this.anotherNumber, this.amount, this.dateTime);
        } else if ("outcome".equals(type)) {
            return String.format("{\"type\":\"%s\",%n\"from\":\"%s\",%n\"amount\":\"%s\",%n\"date\":\"%s\"}",
                    this.type, this.anotherNumber, this.amount, this.dateTime);
        } else throw new IllegalStateException("Type of a customer transaction must be either income or outcome");
    }
}
