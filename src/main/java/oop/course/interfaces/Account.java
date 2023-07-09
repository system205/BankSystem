package oop.course.interfaces;

import oop.course.entity.*;
import oop.course.tools.*;
import oop.course.tools.interfaces.*;

import java.math.*;
import java.time.*;
import java.util.*;

public interface Account extends JSON {
    long balance();

    Transaction transfer(String accountNumber, BigDecimal amount);

    void save(String customerId);

    CustomerRequest attachRequest(String type, BigDecimal amount);

    Collection<CustomerRequest> requests();

    void deposit(BigDecimal amount);

    void withdraw(BigDecimal amount);

    List<Transaction> transactions();

    TransactionStatement compose(LocalDate start, LocalDate end);

    void deactivate();

    AutoPayment createPayment(Form form);

    List<AutoPayment> autopayments();
}
