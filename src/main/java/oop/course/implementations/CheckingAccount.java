package oop.course.implementations;

import oop.course.interfaces.*;
import oop.course.storage.*;

/**
 * A simple bank account
 */
public class CheckingAccount implements Account {
    private final long id;
    private final long balance;

    public CheckingAccount(long id, long balance) {
        this.id = id;
        this.balance = balance;
    }

    public CheckingAccount(Database<String, CheckingAccount> accountDb, String id) {
        CheckingAccount account = accountDb.read(id);
        this.id = account.id;
        this.balance = account.balance;
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

    public String json() {
        return String.format("{\"id\":\"%s\", \"balance\":\"%s\"}", this.id, this.balance);
    }
}
