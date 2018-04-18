package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by p16163779 on 01/03/2018.
 */

public class StarField extends GameObject {
    ArrayList<Star> stars;
    int starNoPerLayer = 50;
    Random random;
    public float scaledWidth, scaledHeight;
    /**
     * <h2>Constructor</h2>
     *
     * @param widthIn
     * @param heightIn
     * @param xIn
     * @param yIn
     */
    public StarField(float widthIn, float heightIn, float xIn, float yIn) {
        super(widthIn, heightIn, xIn, yIn);
        stars = new ArrayList<>();
        random = new Random();
        float starWidth = 20, starHeight = 20, scale = 0.1f, unitWidth = getWidth()/starWidth, unitHeight = getHeight()/starHeight;
        scaledWidth = unitWidth * scale;
        scaledHeight = unitHeight * scale;
    }

    public void generateNewStars(){
        for(int l = 0; l < 3; l++){
            for (int i = 0; i < starNoPerLayer; i++) {
                stars.add(new Star(scaledWidth, scaledHeight, random.nextInt((int) getWidth()), random.nextInt((int) getHeight()),
                        0, random.nextInt(25), l));
            }
        }
    }

    public void update(){
        ArrayList<Star> toReset = new ArrayList<>();
        for(Star star: stars){
            star.update();
            if(star.y > getHeight()){
                toReset.add(star);
            }
            if(!star.active){
                toReset.add(star);
            }
        }
        resetStar(toReset);
    }

    public void resetStar(ArrayList<Star> deadStars){
        for (Star star : deadStars){
            star.resetStar(getWidth(), getHeight());
        }
    }

    public void draw(Paint p, Canvas c){
        for(Star star: stars){
            star.draw(p, c);
        }
    }
}
