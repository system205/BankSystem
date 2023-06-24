package oop.course.entity;

import oop.course.interfaces.*;
import oop.course.storage.*;

public class Customer implements Entity {
    private final long id;
    private final String email;
    private final String name;
    private final String surname;
    private final String password;

    public Customer(String email, String name, String surname, String password) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.id = 0;
    }

    public Customer(Database<Customer> db, long id) {
        final Customer entity = db.read(id);
        this.email = entity.email;
        this.name = entity.name;
        this.surname = entity.surname;
        this.password = entity.password;
        this.id = entity.id;
    }

    @Override
    public String toString() {
        return String.format("email: %s, name: %s, surname: %s", this.email, this.name, this.surname);
    }

    public String toSqlInsert(String table) {
        return String.format("INSERT INTO %s (email, name, surname, password) VALUES (%s, %s, %s, %s)",
                            table, this.email, this.password, this.name, this.surname);
    }
}
