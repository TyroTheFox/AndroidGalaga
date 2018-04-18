package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;

/**
 * Project: Galagalaxian
 * Created by p16163779 on 07/03/2018.
 */

public class ScrollingMessage {

    private TextPanel panel;
    private boolean visible = true;

    public boolean messagePlaying = false;
    private boolean messageBegin = false, messageStopped = false, messageEnded = false;
    public float visibleTime = 10, tickSize = 0.1f, time = 0;

    public ScrollingMessage(){
        panel = new TextPanel(10, 10, 0, 0);
    }

    public ScrollingMessage(float x, float y){
        panel = new TextPanel(10, 10, x, y);
    }

    public void clearLines(){
        panel.clearLines();
    }

    public void addLine(String s, float x, float y){
        panel.addLine(s, x, y);
    }

    public void addLine(String s, float x, float y, int font){
        panel.addLine(s, x, y, font);
    }

    public void setTypeface(Typeface t){
        panel.setAllTypefaces(t);
    }

    public void startMessage(){
        messageBegin = true;
    }

    public void setVisible(boolean v){
        visible = v;
    }

    public void update(Point screenSize){
        if(messageBegin){
            messagePlaying = true;
            panel.setVisible(true);
            if(panel.y < screenSize.y * 0.4f) {
                panel.setPosition(0, lerp(panel.y, screenSize.y * 0.41f, 0.1f));
            } else {
                panel.setPosition(0, screenSize.y * 0.4f);
                messageStopped = true;
            }
        }

        if(messageStopped){
            time += tickSize;
            if (time >= visibleTime) {
                messageEnded = true;
                messageStopped = false;
                time = 0;
            }
        }
        if(messageEnded) {
            messageBegin = false;
            if (panel.y < screenSize.y) {
                panel.setPosition(0, lerp(panel.y, screenSize.y * 1.1f, 0.1f));
            } else {
                panel.setPosition(0, screenSize.y);
                panel.setVisible(false);
                panel.setPosition(0, 0);
                messageEnded = false;
                messagePlaying = false;
            }
        }
    }

    public void draw(Paint p, Canvas c){
        panel.drawAll(p, c);
    }

    public float lerp(float start, float end, float speed)
    {
        return start + speed * (end - start);
    }
}
