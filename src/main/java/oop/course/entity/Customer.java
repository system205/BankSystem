package oop.course.entity;

public class Customer {
    private final String email;
    private final String name;
    private final String surname;
    private final String password;

    public Customer(String email, String name, String surname, String password) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("email: %s, name: %s, surname: %s", this.email, this.name, this.surname);
    }
}
