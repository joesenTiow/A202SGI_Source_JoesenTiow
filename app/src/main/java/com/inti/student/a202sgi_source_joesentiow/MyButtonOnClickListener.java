package com.inti.student.a202sgi_source_joesentiow;

import android.view.View;

public class MyButtonOnClickListener implements View.OnClickListener{
    String genre;
    String title;
    String author;
    String code;
    String description;
    double price;
    final int imageResource;

    MyButtonOnClickListener(String genre, String title, String author, String code, String description, double price, int imageResource) {
        this.genre = genre;
        this.title = title;
        this.author = author;
        this.code = code;
        this.description = description;
        this.price = price;
        this.imageResource = imageResource;
    }

    public void onClick(View v) {

    }
}
