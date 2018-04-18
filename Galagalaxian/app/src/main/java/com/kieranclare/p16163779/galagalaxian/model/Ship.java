package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by p16163779 on 06/02/2018.
 */

public class Ship extends GameObject{
    protected int colour = 0;
    private boolean isBonus = false;
    protected BulletBank bulletBank;
    public float damageMultiplier = 1;
    /**
     * <h2>Constructor</h2>
     *
     * @param width
     * @param height
     * @param xCoord
     * @param yCoord
     * @param colour
     */
    public Ship(float width, float height, float xCoord, float yCoord, int colour) {
        super(width, height, xCoord, yCoord);
        this.colour = colour;
        bulletBank = new BulletBank();
    }

    public void update(){
        bulletBank.updateAll();
    }

    public void drawRect(Paint p, Canvas c){
        p.setColor(colour);
        c.drawRect(x, y, x + getWidth(), y + getHeight(), p);
        bulletBank.drawAll(p, c);
    }

    //Set the width of the rectangle
    public void setWidth (int width){
        super.setWidth(width);
    }

    //Set the height of the rectangle
    public void setHeight (int height){
        super.setHeight(height);
    }

    //Get the width of the rectangle
    public float getWidth() {
        return super.getWidth();
    }

    //Get the height of the rectangle
    public float getHeight (){
        return super.getHeight();
    }

    public void fire(){
        bulletBank.fireNew(x + (getWidth()*0.5f), y, 0, -5, damageMultiplier);
    }

    public ArrayList<Bullet> getAllBullets(){
        return bulletBank.getAllBullets();
    }

    public float lerp(float start, float end, float speed)
    {
        return start + speed * (end - start);
    }
}
