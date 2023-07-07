package oop.course.implementations;

import oop.course.interfaces.*;

import java.math.*;

public class ApprovedRequest implements Transaction {
    private final String type;
    private final BigDecimal amount;

    public ApprovedRequest(String type, BigDecimal amount) {
        this.type = type;
        this.amount = amount;
    }

    @Override
    public String json() {
        return String.format("{\"type\":\"%s\",%n\"amount\":\"%s\"}",
                this.type, this.amount);
    }
}
