package com.inti.student.a202sgi_source_joesentiow;

public class Book {
    private String genre;
    private String title;
    private String author;
    private String code;
    private String description;
    private double price;
    private final int imageResource;

    Book(String genre, String title, String author, String code, String description, double price, int imageResource) {
        this.genre = genre;
        this.title = title;
        this.author = author;
        this.code = code;
        this.description = description;
        this.price = price;
        this.imageResource = imageResource;
    }

    String getGenre() {return genre;}
    String getTitle() {return title;}
    String getAuthor() {return author;}
    String getCode() {return code;}
    String getDescription() {return description;}
    double getPrice() {return price;}

    public int getImageResource() {return imageResource;}
}
