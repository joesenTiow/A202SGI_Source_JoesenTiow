package com.inti.student.a202sgi_source_joesentiow;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderHistoryActivity extends AppCompatActivity {
    UserHelper mUserHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        mUserHelper = new UserHelper(this);

        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        String username = preferences.getString("username", "");

        ArrayList<String> titleList = mUserHelper.getBookOrderHistory(mUserHelper.getUserId(username));
        ArrayList<String> dateList = mUserHelper.getDateHistory(mUserHelper.getUserId(username));

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        OrderHistoryListAdapter adapter = new OrderHistoryListAdapter(this, titleList, dateList);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
