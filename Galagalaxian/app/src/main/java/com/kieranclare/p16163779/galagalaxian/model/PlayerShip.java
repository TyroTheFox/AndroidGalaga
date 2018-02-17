package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by p16163779 on 06/02/2018.
 */

public class PlayerShip extends Ship {
    public float speed = 1;
    public boolean ready = true, firing = false;
    public float fireRate = 7, tickSize = 0.1f, time = 0;
    public float respawnTime = 4, respawnTickSize = 0.1f, respawn = 0;
    public int MaxHP = 1, HP = MaxHP;
    public boolean alive = true;
    /**
     * <h2>Constructor</h2>
     *
     * @param widthIn
     * @param heightIn
     * @param xIn
     * @param yIn
     */
    public PlayerShip(float widthIn, float heightIn, float xIn, float yIn, int colour, float speed) {
        super(widthIn, heightIn, xIn, yIn, colour);
        this.speed = speed;
    }

    public void move(float inX, float inY){
        if(HP > 0) {
            x = lerp(x, inX, speed);
            y = lerp(y, inY, speed);
        }
    }

    @Override
    public void fire(){
        if(ready) {
            bulletBank.fireNew(x + (getWidth()*0.5f), y, 0, -10, damageMultiplier);
            ready = false;
            firing = true;
        }
    }

    public void BulletCollisionCheck(ArrayList<Bullet> bullets){
        for (Bullet bullet : bullets){
            if(circleRectCollision(bullet, this)){
                HP--;
                bullet.life = 0;
            }
        }
    }

    public void ShipCollisionCheck(ArrayList<EnemyShip> ships){
        for (EnemyShip ship : ships){
            if(rectCollision(ship)){
                HP--;
            }
        }
    }

    public void update(float xIn, float yIn, ArrayList<Bullet> bullets, ArrayList<EnemyShip> ships){
        if(HP > 0) {
            move(xIn, yIn);
            rect.set((int) x, (int) y, (int) (x + getWidth()), (int) (y + getHeight()));
            BulletCollisionCheck(bullets);
            ShipCollisionCheck(ships);
        }
            bulletBank.updateAll();
        if(HP > 0) {
            if (firing) {
                time += tickSize;
                if (time >= fireRate) {
                    firing = false;
                    ready = true;
                    time = 0;
                }
            }
        }else{
            alive = false;
            ready = false;
        }

        if(!alive){
            respawn += respawnTickSize;
            if (respawn >= respawnTime) {
                alive = true;
                ready = true;
                respawn = 0;
                HP = MaxHP;
                x = xIn;
                y = yIn;
            }
        }
    }

    @Override
    public void drawRect(Paint p, Canvas c){
        if(HP > 0) {
            p.setColor(colour);
            c.drawRect(x, y, x + getWidth(), y + getHeight(), p);
        }
        bulletBank.drawAll(p, c);
    }

}
