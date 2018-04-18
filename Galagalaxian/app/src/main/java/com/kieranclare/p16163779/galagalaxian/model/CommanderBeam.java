package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

/**
 * Project: Galagalaxian
 * Created by p16163779 on 04/03/2018.
 */

public class CommanderBeam extends GameObject {
    private Sprite beamSprite;
    public RectF commanderBeam;
    private boolean spriteSet = false;
    /**
     * <h2>Constructor</h2>
     *
     * @param widthIn
     * @param heightIn
     * @param xIn
     * @param yIn
     */
    public CommanderBeam(float widthIn, float heightIn, float xIn, float yIn, Bitmap sprite) {
        super(widthIn, heightIn, xIn, yIn);
        commanderBeam = new RectF();
        commanderBeam.set((x - (getWidth()*0.5f)),
                (y + getHeight()),
                (x + (getWidth()*1.5f)),
                (y + getHeight())*4);
        beamSprite = new Sprite(sprite, getWidth(), getHeight(), x, y);
    }

    public void setPosition(float x, float y, Point screenSize){
        commanderBeam.set((x - getWidth()),
                y,
                (x + getWidth()),
                screenSize.y);
        beamSprite.setPosition( commanderBeam.left + (beamSprite.getWidth()*0.5f),
                commanderBeam.top);
        if(!spriteSet) {
            beamSprite.setWidth(commanderBeam.width());
            beamSprite.setHeight(commanderBeam.height());
            beamSprite.addFramesFromMaster(3, 0, 0, (int) (beamSprite.masterBitmap.getWidth() * 0.33f), beamSprite.masterBitmap.getHeight());
            beamSprite.setAnimate(true);
            beamSprite.setRepeat(true);
            spriteSet = true;
        }
    }

    public void update(){
        beamSprite.update();
    }

    public void draw(Paint p, Canvas c){
        p.setColor(Color.BLUE);
        beamSprite.draw(c, p);
        c.drawRect(commanderBeam, p);
        p.reset();
    }
}
