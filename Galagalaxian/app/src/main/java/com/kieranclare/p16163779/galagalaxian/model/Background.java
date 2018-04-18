package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by p16163779 on 01/03/2018.
 */

public class Background extends GameObject {
    StarField starField;
    /**
     * <h2>Constructor</h2>
     *
     * @param widthIn
     * @param heightIn
     * @param xIn
     * @param yIn
     */
    public Background(float widthIn, float heightIn, float xIn, float yIn) {
        super(widthIn, heightIn, xIn, yIn);
        starField = new StarField(getWidth(), getHeight(), x, y);
        starField.generateNewStars();
    }

    public void update(){
        starField.update();
    }

    public void draw(Paint p, Canvas c){
        starField.draw(p, c);
    }
}
