package oop.course.implementations;

import oop.course.interfaces.*;

import java.math.*;

public class SimpleTransaction implements Transaction {
    private final String sender;
    private final String receiver;
    private final BigDecimal amount;

    public SimpleTransaction(String sender, String receiver, BigDecimal amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    @Override
    public String json() {
        return String.format("{\"from\":\"%s\",%n\"to\":\"%s\",%n\"amount\":\"%s\"}",
                this.sender, this.receiver, this.amount);
    }

    @Override
    public BigDecimal balanceChange() {
        return this.amount;
    }
}
