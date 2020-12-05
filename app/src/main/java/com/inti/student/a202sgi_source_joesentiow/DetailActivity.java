package com.inti.student.a202sgi_source_joesentiow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView tvTitleDetail          = findViewById(R.id.tvTitleDetail);
        TextView tvAuthorDetail         = findViewById(R.id.tvAuthorDetail);
        TextView tvCodeDetail           = findViewById(R.id.tvCodeDetail);
        TextView tvDescriptionDetail    = findViewById(R.id.tvDescriptionDetail);
        TextView tvPriceDetail          = findViewById(R.id.tvPriceDetail);
        ImageView ivBookDetail          = findViewById(R.id.ivBookDetail);

        tvTitleDetail.setText(getIntent().getStringExtra("title"));
        tvAuthorDetail.setText("Author: " + getIntent().getStringExtra("author"));
        tvCodeDetail.setText("Product Code: " + getIntent().getStringExtra("code"));
        tvDescriptionDetail.setText(getIntent().getStringExtra("description"));
        tvPriceDetail.setText("Price: RM" + String.valueOf(String.format("%.2f", (getIntent().getDoubleExtra("price", 0)))));

        Glide.with(this)
                .load(getIntent().getIntExtra("image", 0))
                .into(ivBookDetail);
    }
}
