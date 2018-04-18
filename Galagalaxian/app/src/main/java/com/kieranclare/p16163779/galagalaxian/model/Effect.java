package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Map;
import java.util.Random;

/**
 * Created by p16163779 on 28/02/2018.
 */

public class Effect extends GameObject {
    Sprite spriteExplosion;
    SoundEffects soundEffects;
    boolean enemyDeath, soundFired = false;
    Random random = new Random();
    /**
     * <h2>Constructor</h2>
     *  @param widthIn
     * @param heightIn
     * @param xIn
     * @param yIn
     * @param sprites
     * @param enemyDeath
     */
    public Effect(float widthIn, float heightIn, float xIn, float yIn, Map<String, Bitmap> sprites, boolean enemyDeath) {
        super(widthIn, heightIn, xIn, yIn);
        this.enemyDeath = enemyDeath;
        if(enemyDeath){
            spriteExplosion = new Sprite(sprites.get("explode"), getWidth(), getHeight(), x, y);
            spriteExplosion.addFramesFromMaster(5,0, 0, (int) (spriteExplosion.masterBitmap.getWidth() * 0.2f), spriteExplosion.masterBitmap.getHeight());
            spriteExplosion.setAnimate(true);
        }else{
            spriteExplosion = new Sprite(sprites.get("playerexplode"), getWidth()*1.5f, getHeight()*1.5f, x, y);
            spriteExplosion.delay = 20;
            spriteExplosion.addFramesFromMaster(4,0, 0, (int) (spriteExplosion.masterBitmap.getWidth() * 0.25f), spriteExplosion.masterBitmap.getHeight());
            spriteExplosion.setAnimate(true);
        }
    }

    public void setPositon(float xPos, float yPos){
        x = xPos;
        y = yPos;
    }

    public void reset(){
        spriteExplosion.startAnimation();
    }

    public void setSoundEffectObject(SoundEffects soundEffectObject){
        soundEffects = soundEffectObject;
    }

    public void update(){
        spriteExplosion.setPosition(x, y);
        spriteExplosion.update();
        if(soundEffects != null && !soundFired){
            if(enemyDeath){
                if(random.nextBoolean()) {
                    soundEffects.play("galaga_destroyed", 0.8f);
                    soundFired = true;
                }else{
                    soundEffects.play("galaga_destroyed2", 0.8f);
                    soundFired = true;
                }
            }else{
                soundEffects.play("explosion", 0.8f);
                soundFired = true;
            }
        }
    }

    public void draw(Paint p, Canvas c){
        if(!spriteExplosion.end) {
            spriteExplosion.draw(c, p);
        }
    }
}
