package com.kieranclare.p16163779.galagalaxian.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kieranclare.p16163779.galagalaxian.R;
import com.kieranclare.p16163779.galagalaxian.controller.JSONDataHandler;

import org.json.JSONArray;

public class SettingsActivity extends AppCompatActivity {

    JSONDataHandler jdh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        jdh = new JSONDataHandler(this);

        final Button resetbutton = findViewById(R.id.reset_btn);
        setResetOnClick(resetbutton, this, jdh);

        final Button conbutton = findViewById(R.id.control_btn);
        setControlOnClick(conbutton, this, jdh);

        final Button exbutton = findViewById(R.id.backtomenu_btn);
        exbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setResetOnClick(final Button btn, final Context context, final JSONDataHandler jdh){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Are You Sure?");
                alertDialog.setMessage("Are you sure you want to delete the High Scores? Can't get them back if you do.");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Never Mind",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "DO IT!",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Toast toast;
                                String message = "";
                                if(jdh.deleteFile("scoreboard.txt")){
                                    message = "Scoreboard Reset";
                                }else{
                                    message = "Nope, Something Broke...Sorry. Scoreboard Left Intact";
                                }
                                toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                alertDialog.show();
            }
        });
    }

    private void setControlOnClick(final Button btn, final Context context, final JSONDataHandler jdh){
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String[] controlChoices = {
                        "Standard - Touch To Move and Fire",
                        "Tilt - Tilt To Move and Touch to Fire"
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Please Select A Control Style")
                        .setItems(controlChoices, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                JSONArray settings = new JSONArray();
                                switch(which){
                                    case 0:
                                        settings.put(false);
                                        break;
                                    case 1:
                                        settings.put(true);
                                        break;
                                }
                                jdh.writeToFile(settings, "controlsettings.txt");
                                Toast toast = Toast.makeText(context, "Selected: " + controlChoices[which], Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

}
