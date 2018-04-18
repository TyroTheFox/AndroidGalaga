package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Created by p16163779 on 26/02/2018.
 */

public class Sprite extends GameObject {
    boolean animate = false, repeat = false;

    public Bitmap masterBitmap;
    ArrayList<Bitmap> frames;
    int spriteX = 0, spriteY = 0, spriteWidth = 0, spriteHeight = 0;
    boolean flick = false, end = false;
    float delay = 30, tick = 1, frameTime = 0;
    int displayFrame = 0;

    /**
     * <h2>Constructor</h2>
     *
     * @param widthIn
     * @param heightIn
     * @param xIn
     * @param yIn
     */
    public Sprite(Bitmap image, float widthIn, float heightIn, float xIn, float yIn) {
        super(widthIn, heightIn, xIn, yIn);
        masterBitmap = image;
        frames = new ArrayList<>();
    }

    public void setFrameDelay(int delay){
        this.delay = delay;
    }

    public void setAnimate(boolean a){
        animate = a;
    }

    public void setRepeat(boolean r) { repeat = r; }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void startAnimation(){
        end = false;
        displayFrame = 0;
    }

    public void update(){
        if(!end && animate) {
            frameTime += tick;
            if (frameTime >= delay) {
                flick = true;
                frameTime = 0;
            }
        }
    }

    public void addFrameFromMaster(int xPos, int yPos, int sWidth, int sHeight){
        frames.add(setSpriteFromSheet(xPos, yPos, sWidth, sHeight));
    }

    public void addFramesFromMaster(int spriteNo, int xPos, int yPos, int sWidth, int sHeight){
        for(int i = 0; i < spriteNo; i++) {
            frames.add(setSpriteFromSheet(xPos + i, yPos, sWidth, sHeight));
        }
    }

    public Bitmap setSpriteFromSheet(int xPos, int yPos, int sWidth, int sHeight){
        spriteX = xPos;
        spriteY = yPos;
        spriteWidth = sWidth;
        spriteHeight = sHeight;
        Bitmap bmFrame = Bitmap.createBitmap(spriteWidth, spriteHeight, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmFrame);
        Rect src = new Rect((spriteX * spriteWidth), (spriteY * spriteHeight), (spriteX * spriteWidth) + spriteWidth, (spriteY * spriteHeight) + spriteHeight);
        Rect dst = new Rect(0, 0, spriteWidth, spriteHeight);
        c.drawBitmap(masterBitmap, src, dst, null);
        return Bitmap.createScaledBitmap(bmFrame, (int)getWidth(), (int)getHeight(), false);
    }

    public void draw(Canvas c, Paint p, float rotation){
        if(flick){
            displayFrame++;
            if(displayFrame >= frames.size()){
                if(repeat){
                    displayFrame = 0;
                }else{
                    end = true;
                    return;
                }
            }
            flick = false;
        }
        c.save();
            c.rotate(rotation + 90, x + (getWidth()*0.5f), y + (getHeight()*0.5f));
            c.drawBitmap(frames.get(displayFrame), x, y, p);
        c.restore();

    }

    public void draw(Canvas c, Paint p){
        if(flick){
            displayFrame++;
            if(displayFrame >= frames.size()){
                if(repeat){
                    displayFrame = 0;
                }else{
                    end = true;
                    return;
                }
            }
            flick = false;
        }
        c.drawBitmap(frames.get(displayFrame), x, y, p);
    }
}
