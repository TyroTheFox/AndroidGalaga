package com.kieranclare.p16163779.galagalaxian.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.kieranclare.p16163779.galagalaxian.controller.DrawablesManager;
import com.kieranclare.p16163779.galagalaxian.model.Background;
import com.kieranclare.p16163779.galagalaxian.model.Sprite;
import com.kieranclare.p16163779.galagalaxian.R;
/**
 * Created by p16163779 on 19/01/2018.
 */

public class SplashSurfaceView extends SurfaceView implements Runnable{

    Point screenSize; //Holds the screen size
    SurfaceHolder holder; //Surface holder for the canvas
    private boolean ok = false; //Boolean to control pause and resume
    Thread t = null; //Thread for the game logic

    private final static int    MAX_FPS = 60;                   // desired fps
    private final static int    MAX_FRAME_SKIPS = 5;            // maximum number of frames to be skipped
    private final static int    FRAME_PERIOD = 1000 / MAX_FPS;  // the frame period
    private long beginTime;                                     // the time when the cycle began
    public long timeDiff = 0;                                      // the time it took for the cycle to execute
    private long timeDiffCanvas;
    private int sleepTime;                                      // ms to sleep
    private int framesSkipped;

    Paint paint = new Paint();
    Background background;

    DrawablesManager drawablesManager;
    Sprite logo;

    MediaPlayer mediaPlayer;

    public SplashSurfaceView(Context context, Point screenS) {
        super(context);
        holder = getHolder();//Used for the screenview
        screenSize = screenS;
        background = new Background(screenS.x, screenS.y, 0, 0);

        drawablesManager = new DrawablesManager(context);

        Bitmap logoBitmap = drawablesManager.getBitmap("galagagalaxian");
        float logoWidth, logoHeight;

        float imageRatio = logoBitmap.getWidth() / logoBitmap.getHeight();
        float screenRatio = screenSize.x / screenSize.y;
        if(imageRatio <= screenRatio) {
            // The scaled size is based on the height
            logoHeight = screenSize.y;
            logoWidth = logoHeight * imageRatio;
        } else {
            // The scaled size is based on the width
            logoWidth = screenSize.x;
            logoHeight = (int) (logoWidth / imageRatio);
        }

        float logoX = (screenS.x - logoWidth)*0.5f;
        float logoY = (screenS.y - logoHeight)*0.5f;

        logo = new Sprite(drawablesManager.getBitmap("galagagalaxian"), logoWidth, logoHeight, logoX, logoY);
        logo.addFrameFromMaster(0, 0, logo.masterBitmap.getWidth(), logo.masterBitmap.getHeight());

        mediaPlayer = MediaPlayer.create(context, R.raw.splashscreenmusic);
    }

    private void updateCanvas (){
        background.update();
    }

    protected void drawCanvas(Canvas canvas){
        canvas.drawARGB(255, 0, 0, 0); //Add a background colour
        background.draw(paint, canvas);
        logo.draw(canvas, paint);
    }

    public void pressUpdate(boolean shoot){
        if(shoot){
            Intent intent = new Intent(getContext(), MenuActivity.class);
            getContext().startActivity(intent);
        }
    }

    public void run() {
        //Remove conflict between the UI thread and the game thread.
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        while (ok){
            //perform canvas drawing
            if (!holder.getSurface().isValid()) {//if surface is not valid
                continue;//skip anything below it
            }
            Canvas c = holder.lockCanvas(); //Lock canvas, paint canvas, unlock canvas
            synchronized (holder) {
                beginTime = System.currentTimeMillis();
                framesSkipped = 0;  // resetting the frames skipped
                // update game state
                this.updateCanvas();
                timeDiffCanvas = System.currentTimeMillis() - beginTime;
                // render state to the screen
                // draws the canvas on the panel
                this.drawCanvas(c);
                // calculate how long did the cycle take
                timeDiff = System.currentTimeMillis() - beginTime;
                // calculate sleep time
                sleepTime = (int) (FRAME_PERIOD - timeDiff);
                if (sleepTime > 0) {
                    // if sleepTime > 0 put to sleep for short period of time
                    try {
                        // send the thread to sleep for a short period
                        // very useful for battery saving
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
                }

                //ADD THIS IF WE ARE DOING LOTS OF WORK
                //If sleeptime is greater than a frame length, skip a number of frames
                while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
                    // we need to catch up
                    // update without rendering
                    this.updateCanvas();
                    // add frame period to check if in next frame
                    sleepTime += FRAME_PERIOD;
                    framesSkipped++;
                }

                holder.unlockCanvasAndPost(c);
            }
        }
    }

    public void pause(){
        ok = false;
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        while(true){
            try{
                t.join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            break;
        }
        t = null;
    }

    public void resume(){
        ok = true;
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        t = new Thread(this);
        t.start();
    }

    public void stop(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        mediaPlayer = null;
    }
}
