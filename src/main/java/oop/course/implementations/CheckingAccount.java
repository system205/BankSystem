package oop.course.implementations;

import oop.course.entity.*;
import oop.course.interfaces.*;

/**
 * A simple bank account
 * */
public class CheckingAccount implements Account {
    private final long id;
    private final long balance;
    private final Customer owner;

    public CheckingAccount(long id, long balance, Customer owner) {
        this.id = id;
        this.balance = balance;
        this.owner = owner;
    }

    @Override
    public long balance() {
        return 0;
    }

    @Override
    public long spend() {
        return 0;
    }

    @Override
    public long withdraw() {
        return 0;
    }
}
