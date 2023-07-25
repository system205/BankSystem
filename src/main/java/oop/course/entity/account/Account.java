package oop.course.entity.account;

import oop.course.entity.*;
import oop.course.entity.transaction.*;
import oop.course.miscellaneous.*;
import oop.course.miscellaneous.interfaces.*;

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

    void deactivate() throws Exception;

    AutoPayment createPayment(Form form) throws Exception;

    List<AutoPayment> autopayments() throws Exception;
}
