package com.kieranclare.p16163779.galagalaxian.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.kieranclare.p16163779.galagalaxian.model.Bullet;
import com.kieranclare.p16163779.galagalaxian.model.EnemyShip;
import com.kieranclare.p16163779.galagalaxian.model.PlayerShip;
import com.kieranclare.p16163779.galagalaxian.model.Level;

import java.util.ArrayList;

/**
 * Created by p16163779 on 19/01/2018.
 */

public class GameSurfaceView extends SurfaceView implements Runnable{

    Paint paint = new Paint();
    Point screenSize; //Holds the screen size
    SurfaceHolder holder; //Surface holder for the canvas
    private boolean ok = false; //Boolean to control pause and resume
    Thread t = null; //Thread for the game logic

    int gridX = 1, gridY = 1, blockWidth = 100, blockHeight = 100, margin = 10;
    ArrayList<EnemyShip> ships;
    Level sMan;

    PlayerShip playerShip;

    private final static int    MAX_FPS = 60;                   // desired fps
    private final static int    MAX_FRAME_SKIPS = 5;            // maximum number of frames to be skipped
    private final static int    FRAME_PERIOD = 1000 / MAX_FPS;  // the frame period
    private long beginTime;                                     // the time when the cycle began
    private long timeDiff = 0;                                      // the time it took for the cycle to execute
    private long timeDiffCanvas;
    private int sleepTime;                                      // ms to sleep
    private int framesSkipped;

    private float inX, inY;

    Rect gameBoundary;

    public GameSurfaceView(Context context, Point screenS) {
        super(context);
        holder = getHolder();//Used for the screenview
        screenSize = screenS;

        sMan = new Level(gridX, gridY, blockWidth, blockHeight, margin, screenS);
        //sMan.GenerateShips();
        //ships = sMan.getBlockList();

        playerShip = new PlayerShip(150, 150, (screenSize.x * 0.5f) - (150 * 0.5f), (screenSize.y - 100) - (150 * 0.5f), Color.BLUE, 0.1f);
        inX = playerShip.x;
        inY = playerShip.y;

        gameBoundary = new Rect(0, 0, screenSize.x, screenSize.y);
    }

    private void updateCanvas (){
        ArrayList<Bullet> playerShots = playerShip.getAllBullets();
        /*ships.get(0).fire();
        ships.get(0).updateVector(timeDiff);
        ships.get(0).seek(new Vector2(playerShip.x, playerShip.y));*/
        //enemyShots = sMan.updateAll(playerShots, playerShip, timeDiff);
        ArrayList<Bullet> enemyShots = new ArrayList<>();
        sMan.updateAll(playerShots,playerShip,timeDiff);
        enemyShots.addAll(sMan.getEnemyShots());
        if(playerShip.HP <= 0){
            inX = (screenSize.x * 0.5f) - (playerShip.getWidth() * 0.5f);
            inY = (screenSize.y - 100) - (playerShip.getHeight() * 0.5f);
        }
        playerShip.update(inX, inY, enemyShots, sMan.getActiveShips());
    }

    protected void drawCanvas(Canvas canvas){
        canvas.drawARGB(255, 255, 255, 255); //Add a background colour
        sMan.drawAll(paint, canvas);
        /*ball.draw(paint, canvas);*/
        playerShip.drawRect(paint, canvas);
    }

    public void pressUpdate(float xPos, float yPos, boolean shoot){
        inX = xPos - (playerShip.getWidth() * 0.5f);
        inY = yPos - (playerShip.getHeight() * 0.5f);
        if(shoot){
            playerShip.fire();
        }
    }

    public void run() {
        //Remove conflict between the UI thread and the game thread.
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

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
        t = new Thread(this);
        t.start();
    }


}
