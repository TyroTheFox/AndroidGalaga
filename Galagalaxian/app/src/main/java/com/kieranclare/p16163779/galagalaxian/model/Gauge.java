package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Project: Galagalaxian
 * Created by p16163779 on 07/03/2018.
 */

public class Gauge extends GameObject {
    private float percentage = 0;
    private float maximum = 100;
    private boolean visible = true;
    /**
     * <h2>Constructor</h2>
     *
     * @param widthIn
     * @param heightIn
     * @param xIn
     * @param yIn
     */
    public Gauge(float widthIn, float heightIn, float xIn, float yIn) {
        super(widthIn, heightIn, xIn, yIn);
    }

    public Gauge(float widthIn, float heightIn, float xIn, float yIn, float m) {
        super(widthIn, heightIn, xIn, yIn);
        maximum = m;
    }

    public void setVisible(boolean v){
        visible = v;
    }

    public void setMaximum(float m){
        maximum = m;
    }

    public void setCurrentValue(float value){
        percentage = value/maximum;
        if(percentage > maximum/maximum){
            percentage = maximum/maximum;
        }
        if(percentage < 0){
            percentage = 0;
        }
    }

    public void draw(Paint p, Canvas c){
        c.drawRect(x, y, x + (getWidth() * percentage), y + getHeight(), p);
    }
}
