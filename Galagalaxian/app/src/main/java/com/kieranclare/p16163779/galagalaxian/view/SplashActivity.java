package com.kieranclare.p16163779.galagalaxian.view;

import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

public class SplashActivity extends AppCompatActivity {
    private SplashSurfaceView ssv;
    private boolean shoot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash);

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

        ssv = new SplashSurfaceView(this, screenSize);
        setContentView(ssv);
    }

    private void _lockOrientation() {
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
    }

    private void _unlockOrientation() {
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onResume(){
        super.onResume();
        ssv.resume();
        _lockOrientation();
    }

    protected void onPause(){
        super.onPause();
        ssv.pause();
        _unlockOrientation();
    }

    protected void onStop(){
        _unlockOrientation();
        ssv.stop();
        super.onStop();
    }

    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getActionMasked();
        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
// finger touches the screen
                shoot = true;
                break;
            case MotionEvent.ACTION_MOVE:
// finger moves on the screen
                shoot = true;
                break;
            case MotionEvent.ACTION_UP: // finger leaves the screen
                shoot = false;
                break;
        }
// tell the system that we handled the event and no further processing is required
        ssv.pressUpdate(shoot);
        return true;
    }
}
