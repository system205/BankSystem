package oop.course.interfaces;

import oop.course.tools.*;

import java.math.*;

public interface Account extends JSON {
    long balance();

    Transaction transfer(String accountNumber, BigDecimal amount);

    void save(String customerId);
}
