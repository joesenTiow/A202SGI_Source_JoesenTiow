package com.inti.student.a202sgi_source_joesentiow;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AccountSettings extends AppCompatActivity {
    UserHelper mUserHelper;

    EditText mEtUsername, mEtPassword, mEtConfirmPassword;
    Button mBtnSave, mBtnLogout;
    TextView mTvDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        mUserHelper = new UserHelper(this);

        mEtUsername         = findViewById(R.id.etUsername);
        mEtPassword         = findViewById(R.id.etPassword);
        mEtConfirmPassword  = findViewById(R.id.etConfirmPassword);
        mBtnSave            = findViewById(R.id.btnSave);
        mBtnLogout          = findViewById(R.id.btnLogout);
        mTvDelete           = findViewById(R.id.tvDelete);

        final SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);

        final String username = preferences.getString("username", "username");
        final String password = preferences.getString("password", "12345");

        mEtUsername.setText(username);
        mEtPassword.setText(password);
        mEtConfirmPassword.setText(password);

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean checkUsername = mUserHelper.checkUsername(mEtUsername.getText().toString());

                if(checkUsername || mEtUsername.getText().toString().equals(username)) {
                    if (mEtConfirmPassword.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Please confirm password", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (!mEtPassword.getText().toString().equals(mEtConfirmPassword.getText().toString())) {
                            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            SharedPreferences.Editor editor = preferences.edit();

                            Boolean update = mUserHelper.update(username, mEtUsername.getText().toString(), mEtPassword.getText().toString());

                            editor.putString("username", mEtUsername.getText().toString());
                            editor.putString("password", mEtPassword.getText().toString());
                            editor.apply();

                            if (update) {
                                Toast.makeText(getApplicationContext(), "Saved changes", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);

                AlertDialog.Builder alert = new AlertDialog.Builder(AccountSettings.this);
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

                        Intent intent = new Intent(AccountSettings.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alert.setCancelable(false);
                alert.show();
            }
        });

        mTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = preferences.getString("username", "");
                final String password = preferences.getString("password", "");

                AlertDialog.Builder alert = new AlertDialog.Builder(AccountSettings.this);

                final EditText etPassword = new EditText(AccountSettings.this);
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());// set input type to password

                alert.setTitle("Delete account?");
                alert.setMessage("Enter your password to delete your account");
                alert.setView(etPassword);
                alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (etPassword.getText().toString().equals(""))
                            Toast.makeText(getApplicationContext(), "Please enter your password", Toast.LENGTH_SHORT).show();
                        else if (etPassword.getText().toString().equals(password)) {
                            SharedPreferences.Editor editor = preferences.edit();

                            Boolean delete = mUserHelper.delete(username, mUserHelper.getUserId(username));

                            editor.putBoolean("isLoggedIn", false);
                            editor.apply();

                            if (delete) {
                                Toast.makeText(getApplicationContext(), "Successfully deleted", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.setCancelable(false);
                alert.show();
            }
        });
    }
}
