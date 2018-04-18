package com.kieranclare.p16163779.galagalaxian.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.kieranclare.p16163779.galagalaxian.controller.GUI;
import com.kieranclare.p16163779.galagalaxian.controller.Game;
import com.kieranclare.p16163779.galagalaxian.controller.Scoreboard;

/**
 * Created by p16163779 on 19/01/2018.
 */

public class GameSurfaceView extends SurfaceView implements Runnable{

    Point screenSize; //Holds the screen size
    SurfaceHolder holder; //Surface holder for the canvas
    private boolean ok = false; //Boolean to control pause and resume
    Thread t = null; //Thread for the game logic

    Game game;
    GUI gui;

    private final static int    MAX_FPS = 60;                   // desired fps
    private final static int    MAX_FRAME_SKIPS = 5;            // maximum number of frames to be skipped
    private final static int    FRAME_PERIOD = 1000 / MAX_FPS;  // the frame period
    private long beginTime;                                     // the time when the cycle began
    public long timeDiff = 0;                                      // the time it took for the cycle to execute
    private long timeDiffCanvas;
    private int sleepTime;                                      // ms to sleep
    private int framesSkipped;

    private float inX, inY;

    private Game.GameState gameState;

    private int score = 0;

    private boolean paused = false;
    private boolean endingMessageFired = false;
    private Scoreboard scoreboard;

    private int lifeBonus = 500, accuracyBonus = 1000;

    public GameSurfaceView(Context context, Point screenS) {
        super(context);
        holder = getHolder();//Used for the screenview
        screenSize = screenS;

        game = new Game(screenSize, context);
        gui = new GUI(screenSize, context);

        scoreboard = new Scoreboard(context);
    }

    private Game.GameState updateCanvas (){
        return game.update(timeDiff);
    }

    private void updateUI(){
        gui.setScorePosition(game.getPlayerShip().x + game.getPlayerShip().getWidth(),
                game.getPlayerShip().y + (game.getPlayerShip().getHeight()*0.5f));
        gui.setShipStatus(game.getPlayerShip().x - (game.getPlayerShip().getWidth()*0.01f),
                game.getPlayerShip().y + (game.getPlayerShip().getHeight()*0.5f),
                game.getPlayerShip().lives);
        score = game.score;
        gui.setScore(game.score);
        gui.setPausedDisplay(paused);
        gui.update();
    }

    protected void drawCanvas(Canvas canvas){
        game.draw(canvas);
        gui.drawAll(canvas);
    }

    public void pressUpdate(float xPos, float yPos, boolean shoot){
        if(paused){
            gui.attemptReset(shoot);
            if(gui.resetSuccess){
                game.mediaPlayer.stop();
                ((Activity) getContext()).finish();
            }
        }else{
            game.pressUpdate(xPos, yPos, shoot);
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
                if(!gui.messagePlaying && !paused) {
                    gameState = this.updateCanvas();
                }else{
                    gameState = Game.GameState.PAUSED;
                }
                this.updateUI();
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
                    if(!gui.messagePlaying && !paused) {
                        gameState = this.updateCanvas();
                    }else{
                        gameState = Game.GameState.PAUSED;
                    }
                    // add frame period to check if in next frame
                    sleepTime += FRAME_PERIOD;
                    framesSkipped++;
                }

                holder.unlockCanvasAndPost(c);
                switch (gameState){
                    case NEXTLEVEL:
                            int accuracy = game.getAccuracy();
                            int finalscore = Math.round(score + (lifeBonus * game.getPlayerShip().lives) + (accuracyBonus * (accuracy*0.01f)));
                            gui.endLevelMessage(score, accuracy, game.getPlayerShip().lives, finalscore);
                            score = finalscore;
                            game.score = finalscore;
                        break;
                    case LOSE:
                            game.mediaPlayer.stop();
                            GameOver();
                        break;
                    case WIN:
                            game.mediaPlayer.stop();
                            HappyEnd();
                    case PAUSED:
                        game.mediaPlayer.pause();
                    case RUNNING:
                        if(!game.mediaPlayer.isPlaying()){
                            game.mediaPlayer.start();
                        }
                        break;
                }
            }
        }
    }

    /**
     * This Story is Happy End
     */
    private void HappyEnd(){
        if(!endingMessageFired) {
            int accuracy = game.getAccuracy();
            int finalscore = Math.round(score + (lifeBonus * (float)game.getPlayerShip().lives) + (accuracyBonus * ((float)accuracy*0.01f)));
            gui.endLevelMessage(score, accuracy, game.getPlayerShip().lives, finalscore);
            score = finalscore;
            game.score = finalscore;
            endingMessageFired = true;
        }
        if(gui.messageEnd) {
            if(scoreboard.checkScore(score)){
                Intent intent = new Intent(getContext(), NewHighScoreActivity.class);
                intent.putExtra("SCORE", score);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getContext().startActivity(intent);
            }else {
                ((Activity) getContext()).finish();
            }
        }
    }

    private void GameOver(){
        if(!endingMessageFired) {
            int finalscore = Math.round(score);
            gui.gameOverMessage(score, finalscore);
            score = finalscore;
            game.score = finalscore;
            endingMessageFired = true;
        }
        if(gui.messageEnd) {
            if(scoreboard.checkScore(score)){
                Intent intent = new Intent(getContext(), NewHighScoreActivity.class);
                intent.putExtra("SCORE", score);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getContext().startActivity(intent);
            }else {
                ((Activity) getContext()).finish();
            }
        }
    }

    public void pauseGame(){ paused = !paused; }

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
