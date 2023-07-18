package oop.course.client;

public record BankRequest(String id, String accountNumber, String amount, String type, String status) {
}
