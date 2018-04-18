package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by p16163779 on 06/02/2018.
 */

public class BulletBank {
    ArrayList<Bullet> bullets;
    int radius = 10;
    Bitmap sprite;

    public int hitCount = 0;

    public BulletBank(){
        bullets = new ArrayList<Bullet>();
    }

    public void setSprite(Bitmap sprite){
        this.sprite = sprite;
    }

    public void fireNew(float x, float y, float dx, float dy, float damageMultiplier){
        Bullet newBullet = new Bullet(radius, x, y);
        if(sprite != null){
            newBullet.setSprite(sprite);
        }
        newBullet.damage = (int)(newBullet.damage * damageMultiplier);
        newBullet.setAcceleration(dx, dy);
        bullets.add(newBullet);
    }

    public void updateAll(){
        ArrayList<Bullet> toRemove = new ArrayList<>();
        for (int i = 0; i < bullets.size(); i++)
        {
            if(bullets.get(i).active) {
                bullets.get(i).update();
            }else{
                if(bullets.get(i).hitLanded){
                    hitCount++;
                }
                toRemove.add(bullets.get(i));
            }
        }
        bullets.removeAll(toRemove);
    }

    public void drawAll(Paint p, Canvas c){
        for (int i = 0; i < bullets.size(); i++)
        {
            if(bullets.get(i).active) {
                bullets.get(i).draw(p, c);
            }
        }
    }

    public ArrayList<Bullet> getAllBullets(){
        return bullets;
    }

}
