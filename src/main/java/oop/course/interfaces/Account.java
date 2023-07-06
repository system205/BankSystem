package oop.course.interfaces;

import oop.course.entity.*;
import oop.course.tools.*;

import java.math.*;
import java.util.*;

public interface Account extends JSON {
    long balance();

    Transaction transfer(String accountNumber, BigDecimal amount);

    void save(String customerId);

    CustomerRequest attachRequest(String type, BigDecimal amount);

    Collection<CustomerRequest> requests();

    void deposit(BigDecimal amount);

    void withdraw(BigDecimal amount);

}
