package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import java.util.ArrayList;

/**
 * Project: Galagalaxian
 * Created by p16163779 on 05/03/2018.
 */

public class TextPanel extends GameObject {
    ArrayList<UIText> lines;
    boolean visible = true;
    Typeface typeface;
    /**
     * <h2>Constructor</h2>
     *
     * @param widthIn
     * @param heightIn
     * @param xIn
     * @param yIn
     */
    public TextPanel(float widthIn, float heightIn, float xIn, float yIn) {
        super(widthIn, heightIn, xIn, yIn);
        lines = new ArrayList<>();
    }

    public void setAllTypefaces(Typeface t){
        typeface = t;
        for (UIText line : lines){
            line.setTypeface(t);
        }
    }

    public void clearLines(){
        lines.clear();
    }

    public void addLine(UIText text){
        text.setPosition(x + text.x, y + text.y);
        lines.add(text);
    }

    public void addLine(String text, float posX, float posY){
        UIText temp = new UIText(getWidth(), getHeight(), x + posX, y + posY);
        temp.setLocalPosition(posX, posY);
        temp.setDisplayText(text);
        lines.add(temp);
    }

    public void addLine(String text, float posX, float posY, int fontSize){
        UIText temp = new UIText(getWidth(), getHeight(), x + posX, y + posY);
        temp.setLocalPosition(posX, posY);
        temp.setDisplayText(text);
        temp.setFontSize(fontSize);
        lines.add(temp);
    }

    public void setPosition(float xPos, float yPos){
        x = xPos;
        y = yPos;
        for (UIText line : lines){
            line.setPosition(x + line.localX, y + line.localY);
        }
    }

    public void setVisible(boolean v){
        visible = v;
    }

    public void updateAll(){

    }

    public void drawAll(Paint p, Canvas c){
        if(visible) {
            for (UIText line : lines){
                line.draw(p, c);
            }
        }
    }

}
