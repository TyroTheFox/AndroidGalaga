package com.kieranclare.p16163779.galagalaxian.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.kieranclare.p16163779.galagalaxian.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final Button button = findViewById(R.id.start_btn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(myIntent);
            }
        });

        final Button hcbutton = findViewById(R.id.highscore_btn);
        hcbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MenuActivity.this, ScoreboardActivity.class);
                startActivity(myIntent);
            }
        });

        final Button sbutton = findViewById(R.id.settings_btn);
        sbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MenuActivity.this, SettingsActivity.class);
                startActivity(myIntent);
            }
        });

        final Button exbutton = findViewById(R.id.exit_btn);
        exbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finishAffinity();
                System.exit(0);
            }
        });
    }

}
