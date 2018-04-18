package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Project: Galagalaxian
 * Created by p16163779 on 18/04/2018.
 */

public class LifeIcon extends GameObject {
    boolean visible = true;
    RectF rect;
    boolean transition = false;
    public float visibleTime = 2, tickSize = 0.1f, time = 0;
    int opacity = 0, maxOpacity = 255, opacityStep = 10;
    int offsetX = -80, offsetY = 0;

    public LifeIcon(float widthIn, float heightIn, float xIn, float yIn) {
        super(widthIn, heightIn, xIn, yIn);
        rect = new RectF(xIn + offsetX, yIn + offsetY, xIn + widthIn, yIn + heightIn);
    }

    public void setPosition(float xPos, float yPos){
        x = xPos + offsetX;
        y = yPos + offsetY;
        rect.set(x, y, x + getWidth(), y + getHeight());
    }

    public void display(){
        visible = true;
        transition = true;
    }

    public void update(){
        if (transition) {
            opacity += opacityStep;
            if(opacity >= maxOpacity){
                opacity = maxOpacity;
                time += tickSize;
                if (time >= visibleTime) {
                    transition = false;
                    time = 0;
                }
            }
        }else{
            opacity -= opacityStep;
            if(opacity <= 0){
                opacity = 0;
                visible = false;
            }
        }

    }

    public void setVisible(boolean v){
        visible = v;
    }

    public void draw(Paint p, Canvas c){
        if(visible) {
            p.setColor(Color.WHITE);
            c.drawRect(rect, p);
        }
    }
}
