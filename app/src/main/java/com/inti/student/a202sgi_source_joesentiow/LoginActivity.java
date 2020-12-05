package com.inti.student.a202sgi_source_joesentiow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    UserHelper mUserHelper;

    EditText mEtUsername, mEtPassword;
    Button mBtnLogin;
    TextView mTvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserHelper = new UserHelper(this);

        mEtUsername = findViewById(R.id.etUsername);
        mEtPassword = findViewById(R.id.etPassword);
        mBtnLogin   = findViewById(R.id.btnLogin);
        mTvRegister = findViewById(R.id.tvRegister);

        mTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mEtUsername.getText().toString();
                String password = mEtPassword.getText().toString();

                Cursor cursor = mUserHelper.retrieveData();

                Boolean checkUsername = mUserHelper.checkUsername(username);

                if (cursor.getCount() == 0) {
                    Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (username.equals("") || password.equals("")) {
                        Toast.makeText(getApplicationContext(), "Empty Field", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // Check database for matching username
                        while (cursor.moveToNext()) {
                            if (username.equals(cursor.getString(1))) {
                                if (password.equals(cursor.getString(2))) {
                                    Toast.makeText(getApplicationContext(), "Welcome, " + username, Toast.LENGTH_SHORT).show();

                                    SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();

                                    editor.putBoolean("isLoggedIn", true);
                                    //put username and password into SharedPreferences for later use.
                                    editor.putString("username", username);
                                    editor.putString("password", password);

                                    editor.apply();

                                    finish();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Incorrect password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        if (checkUsername) {
                            Toast.makeText(getApplicationContext(), "Username does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}
