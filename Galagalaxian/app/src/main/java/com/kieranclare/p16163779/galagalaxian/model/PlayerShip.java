package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by p16163779 on 06/02/2018.
 */

public class PlayerShip extends Ship {
    private Effect effect;
    public float speed = 1;
    public boolean ready = true, firing = false;
    public float fireRate = 2, tickSize = 0.1f, time = 0;
    public float respawnTime = 4, respawnTickSize = 0.1f, respawn = 0;
    public int MaxHP = 1, HP = MaxHP;
    public int lives = 3;
    public boolean alive = true;
    private float fingerOffset;
    private ArrayList<CapturedShip> capturedShips;
    protected Sprite sprite;
    Matrix matrix = new Matrix();
    SoundEffects soundEffects;

    public int fireCount = 0, shotsHit = 0;

    /**
     * <h2>Constructor</h2>
     * @param widthIn
     * @param heightIn
     * @param xIn
     * @param yIn
     * @param sprites
     * @param soundEffectsObject
     */
    public PlayerShip(float widthIn, float heightIn, float xIn, float yIn, int colour, float speed, Map<String, Bitmap> sprites, SoundEffects soundEffectsObject) {
        super(widthIn, heightIn, xIn, yIn, colour);
        this.speed = speed;
        fingerOffset = this.getWidth() * 2.5f;
        capturedShips = new ArrayList<>();
        sprite = new Sprite(sprites.get("player"), getWidth(), getHeight(), x, y);
        sprite.addFrameFromMaster(0, 0, sprite.masterBitmap.getWidth(), sprite.masterBitmap.getHeight());

        effect = new Effect(getWidth(), getHeight(), x, y, sprites, false);
        effect.setSoundEffectObject(soundEffectsObject);
        soundEffects = soundEffectsObject;
        bulletBank.setSprite(sprites.get("rocket"));
    }

    public Effect getEffect(){
        return effect;
    }

    public void move(float inX, float inY){
        if(HP > 0) {
            x = lerp(x, inX - (getWidth()*0.5f), speed);
            y = lerp(y, inY - fingerOffset, speed);
        }
    }

    @Override
    public void fire(){
        if(ready) {
            bulletBank.fireNew(x + (getWidth()*0.5f), y, 0, -35, damageMultiplier);
            ready = false;
            firing = true;
            soundEffects.play("laser_default", 0.3f);
            fireCount++;
        }
    }

    public void BulletCollisionCheck(ArrayList<Bullet> bullets){
        for (Bullet bullet : bullets){
            if(circleRectCollision(bullet, rect)){
                HP--;
                bullet.life = 0;
            }
        }
    }

    public void ShipCollisionCheck(ArrayList<EnemyShip> ships){
        for (EnemyShip ship : ships){
            if(rectCollision(ship.rect)){
                HP--;
            }
        }
    }

    public ArrayList<CapturedShip> getCapturedShips(){
        return capturedShips;
    }

    public void addCapturedShip(CapturedShip ship){
        capturedShips.add(ship);
    }

    public int getCapturedShipTotal(){
        return capturedShips.size();
    }

    public void update(float xIn, float yIn, ArrayList<Bullet> bullets, ArrayList<EnemyShip> ships){
        if(HP > 0 && lives > 0) {
            move(xIn, yIn);
            rect.set(x, y, x + getWidth(), y + getHeight());
            sprite.setPosition(x, y);
            BulletCollisionCheck(bullets);
            ShipCollisionCheck(ships);
            effect.setPositon(x, y);
        }
        else{
            alive = false;
        }

        bulletBank.updateAll();
        shotsHit += bulletBank.hitCount;
        bulletBank.hitCount = 0;
        ArrayList<CapturedShip> toRemove = new ArrayList<>();
        for(CapturedShip ship : capturedShips){
            if(HP <= 0){
                ship.HP = 0;
            }
            ship.update(x + (ship.getWidth() * ((float)Math.pow(-1, getCapturedShipTotal()))), y - (ship.getHeight() * 1.2f), bullets, ships);
            if(firing){ship.fire();}
            if(ship.HP <=0){
                toRemove.add(ship);
            }
            if(!alive){
                toRemove.add(ship);
            }
        }
        capturedShips.removeAll(toRemove);
        if(HP > 0 && lives > 0) {
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
            effect.update();
            respawn += respawnTickSize;
            if (respawn >= respawnTime) {
                alive = true;
                ready = true;
                respawn = 0;
                HP = MaxHP;
                effect.reset();
                x = xIn;
                y = yIn;
                lives--;
            }
        }
    }

    public boolean isDead(){ return lives <= 0; }

    @Override
    public void drawRect(Paint p, Canvas c){
        if(HP > 0 && lives > 0) {
            p.setColor(colour);
            //c.drawRect(x, y, x + getWidth(), y + getHeight(), p);
            sprite.draw(c, p);
        }else{
            effect.draw(p, c);
        }
        for(CapturedShip ship : capturedShips){
            ship.drawRect(p, c);
        }
        bulletBank.drawAll(p, c);
    }

}
