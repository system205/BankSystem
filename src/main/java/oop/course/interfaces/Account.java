package oop.course.interfaces;

import oop.course.entity.*;
import oop.course.tools.*;
import oop.course.tools.interfaces.*;

import java.math.*;
import java.time.*;
import java.util.*;

public interface Account extends JSON {
    Transaction transfer(String accountNumber, BigDecimal amount) throws Exception;

    void save(String customerId) throws Exception;

    CustomerRequest attachRequest(String type, BigDecimal amount) throws Exception;

    Collection<CustomerRequest> requests() throws Exception;

    void deposit(BigDecimal amount) throws Exception;

    void withdraw(BigDecimal amount) throws Exception;

    List<Transaction> transactions() throws Exception;

    TransactionStatement compose(LocalDate start, LocalDate end) throws Exception;

    void deactivate();

    AutoPayment createPayment(Form form) throws Exception;

    List<AutoPayment> autopayments() throws Exception;
}
