package com.kieranclare.p16163779.galagalaxian.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kieranclare.p16163779.galagalaxian.R;
import com.kieranclare.p16163779.galagalaxian.controller.Scoreboard;

public class NewHighScoreActivity extends AppCompatActivity {

    private Scoreboard scoreboard;
    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_high_score);

        scoreboard = new Scoreboard(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            score = extras.getInt("SCORE");
        }
        final Button button = findViewById(R.id.hssubmitbutton);
        setOnClick(button, score);

    }

    private void setOnClick(final Button btn, final int score){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.hseditText);
                scoreboard.addNewHighScore(editText.getText().toString(), score);
                Intent intent = new Intent(NewHighScoreActivity.this, ScoreboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}