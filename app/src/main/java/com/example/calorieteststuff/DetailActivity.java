package com.example.calorieteststuff;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView foodTitle = findViewById(R.id.titleDetail);
        TextView info = findViewById(R.id.info);
        ImageView sportsImage = findViewById(R.id.sportsImageDetail);

        foodTitle.setText(getIntent().getStringExtra("title"));
        info.setText(getIntent().getStringExtra("info"));
//        Log.d("calories", ""+getIntent().getStringExtra("info"));
        Glide.with(this).load(getIntent().getIntExtra("image_resource",0))
                .into(sportsImage);
    }
}
