package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Bitmap;
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
    public int damage = 1;
    private Sprite sprite;

    public boolean active, hitLanded = false;
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

    public void setSprite(Bitmap sprite){
        this.sprite = new Sprite(sprite, getWidth() * 5, getHeight() * 5, x, y);
        this.sprite.setPosition(x - (sprite.getWidth()*0.5f), y - (sprite.getHeight()*0.5f));
        this.sprite.addFrameFromMaster(0, 0,
                this.sprite.masterBitmap.getWidth(), this.sprite.masterBitmap.getHeight());
    }

    public void draw(Paint p, Canvas c){
        p.setColor(colour);
        if(sprite == null) {
            c.drawCircle(x, y, getWidth(), p);
        }else{
            if(dy < 0) {
                sprite.draw(c, p, -90);
            }else{
                sprite.draw(c, p, 90);
            }
        }
    }

    public void setAcceleration(float dx, float dy){
        this.dx = dx;
        this.dy = dy;
    }

    public void update(){
        x += dx;
        y += dy;
        if(sprite != null) {
            sprite.setPosition(x - (sprite.getWidth() * 0.5f), y - (sprite.getHeight() * 0.5f));
        }
        if(y < 0){
            life = 0;
        }
        life -= decay;
        if(life < 0) {
            active = false;
        }
    }
}
