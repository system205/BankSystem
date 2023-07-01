package oop.course.storage;

import oop.course.implementations.*;

public class CheckingAccountDB implements Database<String, CheckingAccount> {
    @Override
    public CheckingAccount read(String id) {
        return new CheckingAccount(0, 0);
    }

    @Override
    public void write(CheckingAccount account) {
    }
}
