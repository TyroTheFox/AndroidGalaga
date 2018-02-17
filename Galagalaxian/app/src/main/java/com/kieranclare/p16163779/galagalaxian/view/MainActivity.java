package com.kieranclare.p16163779.galagalaxian.view;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    //Create a new GameView object
    private GameSurfaceView gsv;
    private float xPos, yPos;
    private boolean shoot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        //Pass the GameView to setContentView
        //by removing the set content view which used the xml layout
        // Remove this line of code

        //setContentView(R.layout.activity_main);
        // Add these lines of code
        Point screenSize = new Point();
        this.getWindowManager().getDefaultDisplay().getRealSize(screenSize);
        /*gv = new GameView(this, screenSize);
        setContentView(gv);*/
        gsv = new GameSurfaceView(this,screenSize);
        setContentView(gsv);
    }

    @Override
    protected void onResume(){
        super.onResume();
        gsv.resume();
    }

    protected void onPause(){
        super.onPause();
        gsv.pause();
    }

    /* Import MotionEvent must be added*/
    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();
        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
// finger touches the screen
                xPos = event.getX(); yPos = event.getY();
                shoot = true;
                break;
            case MotionEvent.ACTION_MOVE:
// finger moves on the screen
                xPos = event.getX(); yPos = event.getY();
                shoot = true;
                break;
            case MotionEvent.ACTION_UP: // finger leaves the screen
                shoot = false;
                break;
        }
// tell the system that we handled the event and no further processing is required
        gsv.pressUpdate(xPos, yPos, shoot);
        return true;
    }

}
