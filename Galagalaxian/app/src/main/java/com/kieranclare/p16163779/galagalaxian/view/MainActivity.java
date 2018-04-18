package com.kieranclare.p16163779.galagalaxian.view;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.kieranclare.p16163779.galagalaxian.controller.JSONDataHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    //Create a new GameView object
    private GameSurfaceView gsv;
    private float xPos, yPos;
    private boolean shoot = false;

    private float xAccel, xVel = 0.0f;
    private float zAccel, yVel = 0.0f;
    private float xMax, yMax;
    private SensorManager sensorManager;

    boolean sensorMode = false;
    JSONDataHandler jdh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        jdh = new JSONDataHandler(this);
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        //Flags to set the to a full screen without any task bar.
        int mUIFlag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        //Set the flags on the view
        getWindow().getDecorView().setSystemUiVisibility(mUIFlag);

        Point screenSize = new Point();
        this.getWindowManager().getDefaultDisplay().getRealSize(screenSize);

        xPos = screenSize.x * 0.5f;
        yPos = screenSize.y * 0.9f;

        xMax = (float) screenSize.x;
        yMax = (float) screenSize.y;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        setUpSettings();
        gsv = new GameSurfaceView(this, screenSize);
        setContentView(gsv);
    }

    private void setUpSettings(){
        if(jdh.isFileExist("controlsettings.txt")){
            JSONArray settings = new JSONArray();
            try {
                settings = jdh.readFromFile("controlsettings.txt");
                sensorMode = settings.getBoolean(0);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void _lockOrientation() {
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
    }

    private void _unlockOrientation() {
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    protected void onStart() {
        super.onStart();
        if(sensorMode) {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        gsv.resume();
        _lockOrientation();
    }

    protected void onPause(){
        super.onPause();
        gsv.pause();
        _unlockOrientation();
    }

    protected void onStop(){
        _unlockOrientation();
        if(sensorMode) {
            sensorManager.unregisterListener(this);
        }
        super.onStop();
    }

    /* Import MotionEvent must be added*/
    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getActionMasked();
        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
// finger touches the screen
                if(!sensorMode) {
                    xPos = event.getX();
                    yPos = event.getY();
                }
                    shoot = true;
                break;
            case MotionEvent.ACTION_MOVE:
// finger moves on the screen
                if(!sensorMode) {
                    xPos = event.getX();
                    yPos = event.getY();
                }
                    shoot = true;
                break;
            case MotionEvent.ACTION_UP: // finger leaves the screen
                shoot = false;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if(event.getPointerCount() > 1){
                    gsv.pauseGame();
                }
                break;
        }
// tell the system that we handled the event and no further processing is required
        gsv.pressUpdate(xPos, yPos, shoot);
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(sensorMode) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                xAccel = event.values[0];
                zAccel = event.values[2];

                float xS = (xAccel *0.5f) * 5;
                float zS = (zAccel - 5) * 2;

                xPos -= xS;
                yPos -= zS;

                if (xPos > xMax) {
                    xPos = xMax;
                } else if (xPos < 0) {
                    xPos = 0;
                }

                if (yPos > yMax) {
                    yPos = yMax;
                } else if (yPos < 0) {
                    yPos = 0;
                }

                gsv.pressUpdate(xPos, yPos, shoot);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
