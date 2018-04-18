package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by p16163779 on 23/02/2018.
 */

public class CapturedShip extends PlayerShip {
    private Ship masterShip;
    public boolean slaveToEnemy = true;
    /**
     * <h2>Constructor</h2>
     *  @param widthIn
     * @param heightIn
     * @param xIn
     * @param yIn
     * @param speed
     * @param sprites
     */
    public CapturedShip(float widthIn, float heightIn, float xIn, float yIn, float speed, Map<String, Bitmap> sprites, SoundEffects soundEffectsObject) {
        super(widthIn, heightIn, xIn, yIn, Color.GRAY, speed, sprites, soundEffectsObject);
        sprite = new Sprite(sprites.get("captured"), getWidth(), getHeight(), x, y);
        sprite.addFrameFromMaster(0, 0, sprite.masterBitmap.getWidth(), sprite.masterBitmap.getHeight());
    }

    public void setMasterShip(Ship master, boolean enemy){
        masterShip = master;
        slaveToEnemy = enemy;
    }

    public Ship getMasterShip(){
        return masterShip;
    }

    public void ShipCollisionCheck(PlayerShip ship){
        if(rectCollision(ship.rect)) {
            HP--;
        }
    }

    public void fire(){
        if(ready) {
            if(slaveToEnemy) {
                bulletBank.fireNew(x + (getWidth() * 0.5f), y, 0, 25, damageMultiplier);
            }else{
                bulletBank.fireNew(x + (getWidth() * 0.5f), y, 0, -25, damageMultiplier);
            }
            ready = false;
            firing = true;
        }
    }

    public void updateAsEnemy(float xIn, float yIn, ArrayList<Bullet> bullets, PlayerShip playerShip){
        if(HP > 0) {
            x = xIn;
            y = yIn;
            rect.set(x,  y, x + getWidth(), y + getHeight());
            sprite.setPosition(x, y);
            BulletCollisionCheck(bullets);
            ShipCollisionCheck(playerShip);
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

        /*if(!alive){
            respawn += respawnTickSize;
            if (respawn >= respawnTime) {
                alive = true;
                ready = true;
                respawn = 0;
                HP = MaxHP;
                x = xIn;
                y = yIn;
            }
        }*/
    }

    public void update(float xIn, float yIn, ArrayList<Bullet> bullets, ArrayList<EnemyShip> ships){
        if(HP > 0) {
            x = xIn;
            y = yIn;
            rect.set(x, y, x + getWidth(), y + getHeight());
            sprite.setPosition(x, y);
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
    }
}
