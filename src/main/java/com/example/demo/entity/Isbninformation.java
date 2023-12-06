package com.example.demo.entity;

public class Isbninformation {
    String isbn;
    String book_name;
    double book_price;

    public Isbninformation(String isbn, String book_name, double book_price) {
        this.isbn = isbn;
        this.book_name = book_name;
        this.book_price = book_price;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public double getBook_price() {
        return book_price;
    }

    public void setBook_price(double book_price) {
        this.book_price = book_price;
    }
}
