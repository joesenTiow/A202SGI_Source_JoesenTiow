package com.inti.student.a202sgi_source_joesentiow;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;

public class UserHelper extends SQLiteOpenHelper {
    UserHelper(Context context) {
        super(context, "Users.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table user (userId integer primary key autoincrement, " +
                "username text, password text)");
        db.execSQL("Create table book (userId integer, title text, date text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
        db.execSQL("drop table if exists book");
    }

    public boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);

        long ins = db.insert("user", null, values);

        return ins != -1;
    }

    public void insertBook(int userId, String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        String currentDateTime = java.text.DateFormat.getDateTimeInstance().format(new Date());
        ContentValues values = new ContentValues();
        values.put("userId", userId);
        values.put("title", title);
        values.put("date", currentDateTime);

        db.insert("book", null, values);
    }

    public Boolean checkUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from user where username = ?",
                new String[] {username});

        // Returns true if no other user has the same username
        return cursor.getCount() <= 0;
    }

    public Cursor retrieveData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from user", null);
    }

    public boolean update(String oldUsername, String newUsername, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (oldUsername.equals(newUsername)) {
            db.execSQL("update user set password = '" + newPassword + "' where username = '" + oldUsername + "'");
        }
        else {
            db.execSQL("update user set username = '" + newUsername + "' where username = '" + oldUsername + "'");
            db.execSQL("update user set password = '" + newPassword + "' where username = '" + newUsername + "'");
        }
        return true;
    }

    public boolean delete(String username, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from user where username = '" + username + "' ");
        db.execSQL("delete from book where userId = '" + userId + "' ");

        return true;
    }

    public int getUserId(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from user where username = ?", new String[] {username});

        cursor.moveToFirst();

        return cursor.getInt(0);
    }

    public ArrayList<String> getBookOrderHistory(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> bookTitleList = new ArrayList<>();

        Cursor cursor = db.rawQuery("Select * from book where userId = ?", new String[] {Integer.toString(userId)});

        if (cursor.moveToFirst()) {
            do {
                String bookTitle;
                bookTitle = cursor.getString(1);

                bookTitleList.add(bookTitle);
            } while (cursor.moveToNext());
        }

        return bookTitleList;
    }

    public ArrayList<String> getDateHistory(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> dateList = new ArrayList<>();

        Cursor cursor = db.rawQuery("Select * from book where userId = ?", new String[] {Integer.toString(userId)});

        if (cursor.moveToFirst()) {
            do {
                String date;
                date = cursor.getString(2);

                dateList.add(date);
            } while (cursor.moveToNext());
        }

        return dateList;
    }
}
