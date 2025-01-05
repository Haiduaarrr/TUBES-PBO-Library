package com.example.library;

public class Transaction {
    private int id;
    private String bookTitle;
    private String transactionDate;
    private String returnDate;
    private String status;

    public Transaction(int id, String bookTitle, String transactionDate, String returnDate, String status) {
        this.id = id;
        this.bookTitle = bookTitle;
        this.transactionDate = transactionDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public String getStatus() {
        return status;
    }
}
