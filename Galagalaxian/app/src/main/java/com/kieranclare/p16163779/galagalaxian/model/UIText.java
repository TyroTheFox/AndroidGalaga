package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * Project: Galagalaxian
 * Created by p16163779 on 05/03/2018.
 */

public class UIText extends GameObject {
    String DisplayText = "";
    boolean visible = true;
    int fontSize = 20;
    float localX = 0, localY = 0;
    Typeface typeface;
    /**
     * <h2>Constructor</h2>
     *  @param widthIn
     * @param heightIn
     * @param xIn
     * @param yIn
     */
    public UIText(float widthIn, float heightIn, float xIn, float yIn) {
        super(widthIn, heightIn, xIn, yIn);
    }

    public void setTypeface(Typeface t){
        typeface = t;
    }

    public void setDisplayText(String text){
        if(!DisplayText.matches(text)) {
            DisplayText = text;
        }
    }

    public void setFontSize(int f){
        fontSize = f;
    }

    public void setPosition(float xPos, float yPos){
        x = xPos;
        y = yPos;
    }

    public void setLocalPosition(float xPos, float yPos){
        localX = xPos;
        localY = yPos;
    }

    public void setVisible(boolean v){
        visible = v;
    }

    public void draw(Paint p, Canvas c){
        if(visible) {
            if(typeface != null){
                p.setTypeface(typeface);
            }
            p.setColor(Color.WHITE);
            p.setTextSize(fontSize);
            c.drawText(DisplayText, x, y, p);
        }
    }
}
