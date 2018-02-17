package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by p16163779 on 06/02/2018.
 */

public class Bullet extends GameObject {

    private int colour = Color.RED;
    public float dx = 0, dy = 0;
    public float lifeSpan = 20, life = lifeSpan, decay = 0.1f;
    public int damage = 5;

    public boolean active;
    /**
     * <h2>Constructor</h2>
     *
     * @param radius
     * @param xIn
     * @param yIn
     */
    public Bullet(int radius, float xIn, float yIn) {
        super(radius, radius, xIn, yIn);
        active = true;
    }

    public void draw(Paint p, Canvas c){
        p.setColor(colour);
        c.drawCircle(x, y, getWidth(), p);
    }

    public void setAcceleration(float dx, float dy){
        this.dx = dx;
        this.dy = dy;
    }

    public void update(){
        x += dx;
        y += dy;
        life -= decay;
        if(life < 0) {
            active = false;
        }
    }
}
