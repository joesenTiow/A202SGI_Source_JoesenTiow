package com.inti.student.a202sgi_source_joesentiow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    UserHelper mUserHelper;

    EditText mEtUsername, mEtPassword, mEtConfirmPassword;
    Button mBtnRegister;
    TextView mTvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUserHelper = new UserHelper(this);

        mEtUsername         = findViewById(R.id.etUsername);
        mEtPassword         = findViewById(R.id.etPassword);
        mEtConfirmPassword  = findViewById(R.id.etConfirmPassword);
        mBtnRegister        = findViewById(R.id.btnRegister);
        mTvLogin            = findViewById(R.id.tvLogin);

        mTvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mEtUsername.getText().toString();
                String password = mEtPassword.getText().toString();
                String confirmPassword = mEtConfirmPassword.getText().toString();

                if (username.equals("") || password.equals("") || confirmPassword.equals("")) {
                    Toast.makeText(getApplicationContext(), "Empty Field", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (password.equals(confirmPassword)) {
                        Boolean checkUsername = mUserHelper.checkUsername(username);
                        if (checkUsername) {
                            // Insert username and password into database
                            Boolean insertUser = mUserHelper.insertUser(username, password);

                            if (insertUser) {
                                Toast.makeText(getApplicationContext(), "Successfully registered", Toast.LENGTH_SHORT).show();

                                SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();

                                editor.putBoolean("isLoggedIn", true);
                                //put username and password into SharedPreferences for later use
                                editor.putString("username", username);
                                editor.putString("password", password);

                                editor.apply();

                                finish();
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
