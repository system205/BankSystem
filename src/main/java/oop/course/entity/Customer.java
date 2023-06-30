package oop.course.entity;

import oop.course.interfaces.*;
import oop.course.storage.*;
import oop.course.tools.interfaces.*;

public class Customer implements Entity {
    private final long id;
    private final String email;
    private final String name;
    private final String surname;
    private final String password;

    public Customer(String email, String name, String surname, String password) {
        this(0, email, name, surname, password);
    }

    public Customer(Database<Long, Customer> db, long id) {
        final Customer entity = db.read(id);
        this.email = entity.email;
        this.name = entity.name;
        this.surname = entity.surname;
        this.password = entity.password;
        this.id = entity.id;
    }

    public Customer(long id, String email, String name, String surname, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.password = password;
    }

    public Customer(Form form) {
        this.id = form.longField("id");
        this.email = form.stringField("email");
        this.name = form.stringField("name");
        this.surname = form.stringField("surname");
        this.password = form.stringField("password");
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
