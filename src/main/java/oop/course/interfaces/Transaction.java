package oop.course.interfaces;

public interface Transaction {
    Response transfer(String senderNumber, String receiverNumber);
}
