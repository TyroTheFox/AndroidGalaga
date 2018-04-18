package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Project: Galagalaxian
 * Created by p16163779 on 05/03/2018.
 */

public class UIHUDText extends UIText {
    boolean transition = false;
    public float visibleTime = 2, tickSize = 0.1f, time = 0;

    int opacity = 0, maxOpacity = 255, opacityStep = 10;
    /**
     * <h2>Constructor</h2>
     *  @param widthIn
     * @param heightIn
     * @param xIn
     * @param yIn
     */
    public UIHUDText(float widthIn, float heightIn, float xIn, float yIn) {
        super(widthIn, heightIn, xIn, yIn);
        visible = false;
    }

    public void setDisplayText(String text){
        if(!DisplayText.matches(text)) {
            DisplayText = text;
            visible = true;
            transition = true;
        }
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

    public void draw(Paint p, Canvas c){
        if(visible) {
            p.setColor(Color.WHITE);
            p.setAlpha(opacity);
            p.setTextSize(fontSize);
            c.drawText(DisplayText, x, y, p);
        }
    }
}
