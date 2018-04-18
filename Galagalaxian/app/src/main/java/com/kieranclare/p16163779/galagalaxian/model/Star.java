package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Random;

/**
 * Created by p16163779 on 01/03/2018.
 */

public class Star extends GameObject {
    private Vector2 velocity;
    private Color drawColor;
    private StarType currentStarType;
    private int currentLayer;
    private Random random = new Random();
    private int min = 0, max = 7;

    public float lifeSpan = 20, life = lifeSpan, decay = 0.1f;
    public boolean active = true;

    public enum StarType
    {
        Star1(50), // Dark
        Star2(70), // Dark
        Star3(110), // Medium
        Star4(130), // Medium
        Star5(170), // Light
        Star6(200), // Light
        Star7(255);  // Light
        int value = 0;
        private StarType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private RectF rect;

    /**
     * <h2>Constructor</h2>
     *
     * @param widthIn
     * @param heightIn
     * @param xIn
     * @param yIn
     */
    public Star(float widthIn, float heightIn, float xIn, float yIn, float dx, float dy, int layer) {
        super(widthIn, heightIn, xIn, yIn);
        rect = new RectF(x, y, x + getWidth(), y + getHeight());
        velocity = new Vector2(dx, dy);
        currentLayer = layer;

        switch (currentLayer){
            case (0):
                min = 5;
                max = 7;
                break;
            case (1):
                velocity.multiply(0.6f);
                min = 3;
                max = 5;
                break;
            case (2):
                velocity.multiply(0.25f);
                min = 1;
                max = 3;
                break;
        }

        if(velocity.y < 1){
            velocity.y = 1;
        }

        switch (getBrightness()){
            case (1):
                currentStarType = StarType.Star1;
                break;
            case (2):
                currentStarType = StarType.Star2;
                break;
            case (3):
                currentStarType = StarType.Star3;
                break;
            case (4):
                currentStarType = StarType.Star4;
                break;
            case (5):
                currentStarType = StarType.Star5;
                break;
            case (6):
                currentStarType = StarType.Star6;
                break;
            case (7):
                currentStarType = StarType.Star7;
                break;
        }
    }

    public void resetStar(float width, float height){
        x = random.nextInt((int)width);
        y = -getHeight();
        active = true;
        life = lifeSpan;
    }

    public int getBrightness(){
        int temp = random.nextInt(max)+1;
        if(temp < min){
            temp = getBrightness();
        }
        return temp;
    }

    public void update(){
        x += velocity.x;
        y += velocity.y;
        rect.set(x, y, x + getWidth(), y + getHeight());
    }

    public void draw(Paint p, Canvas c)
    {
        if(active){
            p.setARGB(currentStarType.getValue(), 255, 255, 255);
            c.drawRect(rect, p);
            p.reset();
        }
    }
}
