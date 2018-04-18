package com.kieranclare.p16163779.galagalaxian.controller;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Typeface;

import com.kieranclare.p16163779.galagalaxian.model.Gauge;
import com.kieranclare.p16163779.galagalaxian.model.LifeIcon;
import com.kieranclare.p16163779.galagalaxian.model.ScrollingMessage;
import com.kieranclare.p16163779.galagalaxian.model.TextPanel;
import com.kieranclare.p16163779.galagalaxian.model.UIHUDText;

import java.util.Vector;

/**
 * Project: Galagalaxian
 * Created by p16163779 on 05/03/2018.
 */

public class GUI {
    Paint paint = new Paint();
    Point screenSize; //Holds the screen size

    Typeface typeface;
    UIHUDText score;
    ScrollingMessage levelEnd, gameOver, startMessage, currentMessage = null;
    Gauge gauge;
    public boolean messagePlaying = false, messageEnd = false, resetSuccess = false;
    private boolean reset = false, gamePaused = false;

    TextPanel paused;

    int latestScore = 0;

    public float timeToReset = 10, tickSize = 0.1f, time = 0;

    PointF playerPosition;
    int lives = 0;

    Vector<LifeIcon> lifeIcons;

    public GUI(Point screenS, Context context){
        screenSize = screenS;
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/magmawave.otf");

        score = new UIHUDText(100, 50, 0, 0);
        score.setTypeface(typeface);
        score.setDisplayText("0");
        score.setFontSize(60);

        paused = new TextPanel(100, 50, 10, screenSize.y*0.4f);
        paused.setVisible(false);
        paused.addLine("Paused", 10, 10, 60);
        paused.addLine("Tap with Two Fingers to Resume", 10, 70, 40);
        paused.addLine("Touch and Hold to Return to Menu", 10, 130, 40);
        paused.setAllTypefaces(typeface);

        startMessage = new ScrollingMessage(0, screenSize.y * -0.1f);
        startMessage.setVisible(false);

        levelEnd = new ScrollingMessage(0, screenSize.y * -0.1f);
        levelEnd.setVisible(false);

        gameOver = new ScrollingMessage( 0, screenSize.y * -0.1f);
        gameOver.setVisible(false);

        gauge = new Gauge(screenSize.x, 50, 0, paused.y + 190, timeToReset);

        playerPosition = new PointF();
        lifeIcons = new Vector<>();
    }

    public void setScorePosition(float xPos, float yPos){
        score.setPosition(xPos, yPos);
    }

    public void setShipStatus(float xPos, float yPos, int lives){
        playerPosition.set(xPos, yPos);
        if(this.lives != lives) {
            this.lives = lives;
            lifeIcons.clear();
            for(int i = 0; i < lives; i++) {
                lifeIcons.add(new LifeIcon(20, 20, playerPosition.x - (40 * i), playerPosition.y));
                lifeIcons.lastElement().display();
            }
        }
    }

    public void setPausedDisplay(boolean p){
        paused.setVisible(p);
        gamePaused = p;
        if(!p){
            resetSuccess = false;
            reset = false;
            time = 0;
        }
    }

    public void endLevelMessage(int score, int accuracy, int lives, int finalscore){
        levelEnd.clearLines();
        levelEnd.addLine("SUCCESS", 10, 10, 80);
        levelEnd.addLine("Score: " + score, 50, 120, 60);
        levelEnd.addLine("Accuracy: " + accuracy + "%", 50, 180, 60);
        levelEnd.addLine("Lives: " + lives, 50, 240, 60);
        levelEnd.addLine("Final Score", 10, 380, 100);
        levelEnd.addLine("" + finalscore, 10, 480, 100);
        levelEnd.setTypeface(typeface);
        currentMessage = levelEnd;
        messagePlaying = true;
        messageEnd = false;
        levelEnd.startMessage();
    }

    public void gameOverMessage(int score, int finalscore){
        gameOver.clearLines();
        gameOver.addLine("BAD LUCK!", 10, 10, 80);
        gameOver.addLine("Score: " + score, 50, 120, 60);
        gameOver.addLine("Final Score: " + finalscore, 10, 360, 100);
        gameOver.setTypeface(typeface);
        currentMessage = gameOver;
        messagePlaying = true;
        messageEnd = false;
        gameOver.startMessage();
    }

    public void attemptReset(boolean r){
        reset = r;
    }

    public void update(){
        score.update();
        int representedLife = 0;
        for (LifeIcon icon: lifeIcons) {
            icon.setPosition(playerPosition.x - (40 * representedLife), playerPosition.y);
            icon.update();
            representedLife++;
        }
        if(currentMessage != null) {
            currentMessage.update(screenSize);
            if(!currentMessage.messagePlaying){
                messagePlaying = false;
                currentMessage = null;
                messageEnd = true;
            }
        }

        if(reset) {
            time += tickSize;
            gauge.setCurrentValue(time);
            if (time >= timeToReset) {
                time = 0;
                gauge.setCurrentValue(time);
                resetSuccess = true;
            }
        }else{
            resetSuccess = false;
            time = 0;
        }
    }

    public void setScore(int s){
        latestScore = s;
        score.setDisplayText("" + s);
    }

    public void drawAll(Canvas c){
        if(currentMessage != null) {
            currentMessage.draw(paint, c);
        }
        score.draw(paint, c);
        for (LifeIcon icon: lifeIcons) {
            icon.draw(paint, c);
        }
        if(gamePaused){
            c.drawARGB(70, 0, 0, 0);
        }
        paused.drawAll(paint, c);
        if(reset) {
            gauge.draw(paint, c);
        }
    }

    public float lerp(float start, float end, float speed)
    {
        return start + speed * (end - start);
    }
}
