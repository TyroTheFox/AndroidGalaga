package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by p16163779 on 06/02/2018.
 */

public class BulletBank {
    ArrayList<Bullet> bullets;

    public BulletBank(){
        bullets = new ArrayList<Bullet>();
    }

    public void fireNew(float x, float y, float dx, float dy, float damageMultiplier){
        Bullet newBullet = new Bullet(10, x, y);
        newBullet.damage = (int)(newBullet.damage * damageMultiplier);
        newBullet.setAcceleration(dx, dy);
        bullets.add(newBullet);
    }

    public void updateAll(){
        ArrayList<Bullet> toRemove = new ArrayList<Bullet>();
        for (Bullet bullet : bullets)
        {
            if(bullet.active) {
                bullet.update();
            }else{
                toRemove.add(bullet);
            }
        }
        bullets.removeAll(toRemove);
    }

    public void drawAll(Paint p, Canvas c){
        for (Bullet bullet : bullets)
        {
            bullet.draw(p, c);
        }
    }

    public ArrayList<Bullet> getAllBullets(){
        return bullets;
    }

}
