package com.kieranclare.p16163779.galagalaxian.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.kieranclare.p16163779.galagalaxian.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Button button = findViewById(R.id.startbtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(SplashActivity.this, MenuActivity.class);
                startActivity(myIntent);
            }
        });
    }
}
