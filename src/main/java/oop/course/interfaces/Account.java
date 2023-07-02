package oop.course.interfaces;

import java.math.*;

public interface Account {
    long balance();

    Transaction transfer(String accountNumber, BigDecimal amount);
}
