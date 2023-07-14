package oop.course.interfaces;

import oop.course.entity.*;
import oop.course.tools.*;

import java.math.*;
import java.util.*;

public interface Account extends JSON {
    long balance() throws Exception;

    Transaction transfer(String accountNumber, BigDecimal amount) throws Exception;

    void save(String customerId) throws Exception;

    CustomerRequest attachRequest(String type, BigDecimal amount) throws Exception;

    Collection<CustomerRequest> requests();

    void deposit(BigDecimal amount);

    void withdraw(BigDecimal amount);

}
