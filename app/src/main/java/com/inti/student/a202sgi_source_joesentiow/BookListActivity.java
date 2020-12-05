package com.inti.student.a202sgi_source_joesentiow;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity {
    private ArrayList<Book> mBookData;
    private BookAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mBookData = new ArrayList<>();

        mAdapter = new BookAdapter(this, mBookData);

        recyclerView.setAdapter(mAdapter);

        initializeData();
    }

    private void initializeData() {
        String[] bookGenres         = getResources().getStringArray(R.array.book_genres);
        String[] bookTitles         = getResources().getStringArray(R.array.book_titles);
        String[] bookAuthors        = getResources().getStringArray(R.array.book_authors);
        String[] bookCodes          = getResources().getStringArray(R.array.book_codes);
        String[] bookPrices         = getResources().getStringArray(R.array.book_prices);
        String[] bookDescriptions   = getResources().getStringArray(R.array.book_descriptions);

        TypedArray bookResImage = getResources().obtainTypedArray(R.array.book_images);

        // Clear the existing data to avoid duplication
        mBookData.clear();

        // Create an ArrayList of books
        for (int i = 0; i < bookTitles.length; i++) {
            if (bookGenres[i].equals(getIntent().getStringExtra("genre"))) {
                mBookData.add(new Book(
                        bookGenres[i],
                        bookTitles[i],
                        bookAuthors[i],
                        bookCodes[i],
                        bookDescriptions[i],
                        Double.parseDouble(bookPrices[i]),
                        bookResImage.getResourceId(i, 0)
                ));
            }
        }

        // Clean up data in variable
        bookResImage.recycle();

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final ArrayPreferences arrayPreferences = new ArrayPreferences(this);
        final ArrayList<String> bookArray = arrayPreferences.getListString("bookArray");

        switch(item.getItemId()) {
            case R.id.itAccountSettings:
                SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

                if (isLoggedIn) {
                    Intent intent = new Intent(BookListActivity.this, AccountSettings.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(BookListActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.itCart:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                isLoggedIn = preferences.getBoolean("isLoggedIn", false);

                if (isLoggedIn) {
                    final String username = preferences.getString("username", "");
                    String bookList = preferences.getString("bookList", "");
                    float totalPrice = preferences.getFloat("totalPrice", 0);

                    alert.setTitle(username + "'s cart");
                    alert.setMessage(bookList
                            + "\n--------------------------------------------------\nTotal\n"
                            + String.format("%50s", "RM" + String.format("%.2f", totalPrice)));

                    alert.setPositiveButton("Checkout", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("bookList", "");
                            editor.putFloat("totalPrice", 0);
                            editor.apply();

                            UserHelper mUserHelper = new UserHelper(getApplicationContext());

                            for (int i = 0; i < bookArray.size(); i++)
                                mUserHelper.insertBook(mUserHelper.getUserId(username), bookArray.get(i));

                            Toast.makeText(getApplicationContext(), bookArray.size() + " book(s) purchased", Toast.LENGTH_SHORT).show();

                            bookArray.clear();
                            arrayPreferences.putListString("bookArray", bookArray);
                        }
                    });

                    alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                }
                else {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("bookList", "");
                    editor.putFloat("totalPrice", 0);
                    editor.apply();

                    alert.setTitle("Not Logged In");
                    alert.setMessage("Please log in to view cart.");

                    alert.setPositiveButton("Log in", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                }

                alert.setCancelable(false);
                alert.show();
                break;
            case R.id.itOrderHistory:
                preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                isLoggedIn = preferences.getBoolean("isLoggedIn", false);

                String username = preferences.getString("username", "");

                if (isLoggedIn) {
                    Intent intent = new Intent(BookListActivity.this, OrderHistoryActivity.class);
                    startActivity(intent);

                    UserHelper mUserHelper = new UserHelper(getApplicationContext());
                    ArrayList<String> titleList = mUserHelper.getBookOrderHistory(mUserHelper.getUserId(username));

                    if (titleList.isEmpty())
                        Toast.makeText(getApplicationContext(), "Your order history appears here", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(BookListActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.itLogout:
                preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                isLoggedIn = preferences.getBoolean("isLoggedIn", false);

                if (isLoggedIn) {
                    alert = new AlertDialog.Builder(this);
                    alert.setTitle("Log out");

                    if (preferences.getString("bookList", "").equals(""))
                        alert.setMessage("Are you sure you want to log out?");
                    else
                        alert.setMessage("You have items in your cart. Are you sure you want to log out?");

                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("isLoggedIn", false);
                            editor.putString("bookList", "");
                            editor.putFloat("totalPrice", 0);
                            editor.apply();
                            Toast.makeText(getApplicationContext(), "Successfully logged out", Toast.LENGTH_SHORT).show();

                            bookArray.clear();
                            arrayPreferences.putListString("bookArray", bookArray);
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    alert.setCancelable(false);
                    alert.show();

                    break;
                }
                else {
                    Toast.makeText(getApplicationContext(), "Already logged out", Toast.LENGTH_SHORT).show();
                    break;
                }
        }
        return super.onOptionsItemSelected(item);
    }
}
