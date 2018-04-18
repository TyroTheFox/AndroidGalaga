package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by p16163779 on 13/02/2018.
 */

public class BlueBug extends EnemyShip implements Visitable{
    int score = 25;
    Sprite spriteIdle;
    private Effect effect;
    SoundEffects soundEffects;
    boolean fired = false;
    /**
     * @param widthIn
     * @param heightIn
     * @param xIn
     * @param yIn
     * @param speed
     * @param sprites
     * @param soundEffectsObject
     */
    public BlueBug(float widthIn, float heightIn, float xIn, float yIn, float speed, Map<String, Bitmap> sprites, SoundEffects soundEffectsObject) {
        super(widthIn, heightIn, xIn, yIn, Color.BLUE, speed);
        eType = enemyType.BLUE;
        this.spriteIdle = new Sprite(sprites.get("blue"), getWidth(), getHeight(), x, y);
        this.spriteIdle.addFrameFromMaster(0, 0, spriteIdle.masterBitmap.getWidth(), (int)(spriteIdle.masterBitmap.getHeight()));

        effect = new Effect(getWidth(), getHeight(), x, y, sprites, true);
        effect.setSoundEffectObject(soundEffectsObject);
        soundEffects = soundEffectsObject;
        bulletBank.setSprite(sprites.get("rocket"));
    }

    public Effect getEffect(){
        return effect;
    }

    @Override
    public void fire(){
        if(ready) {
            bulletBank.fireNew(x + (getWidth()*0.5f), y + getHeight(), 0, 25, damageMultiplier);
            ready = false;
            firing = true;
            soundEffects.play("laser_default", 0.3f);
        }
    }

    @Override
    public void update(ArrayList<Bullet> bullets, PlayerShip ship, long delta){
        super.update(bullets, ship, delta);
        spriteIdle.setPosition(x, y);
        effect.setPositon(x, y);
    }

    @Override
    public void drawRect(Paint p, Canvas c){
        super.drawRect(p, c);
        if(HP > 0) {
            spriteIdle.draw(c, p, rotation);
        }
    }

    public void generateUniqueAttackPattern(Point screenSize, GridPattern gridPattern, PointF t){
        AttackPattern temp = new AttackPattern(delay);
        temp.attackName = 7;
        temp.moveTo(x, y);

        temp.lineTo(t.x + getWidth(), screenSize.y * 0.7f);

        if(t.x >= screenSize.x*0.5f) {
            temp.lineTo(screenSize.x * 0.1f, screenSize.y * 0.4f);
            temp.lineTo(screenSize.x * 0.9f, screenSize.y * 0.5f);
            temp.lineTo(screenSize.x * 0.1f, screenSize.y * 0.5f);
        }else{
            temp.lineTo(screenSize.x * 0.9f, screenSize.y * 0.4f);
            temp.lineTo(screenSize.x * 0.1f, screenSize.y * 0.5f);
            temp.lineTo(screenSize.x * 0.9f, screenSize.y * 0.5f);
        }

        int tempInt = 2;
        gridPattern.ships.get(tempInt)[gridx][gridy] = this;
        temp.lineTo(gridPattern.getPosition(gridx, gridy, tempInt)[0],
                gridPattern.getPosition(gridx, gridy, tempInt)[1]);
        addPath(temp);
    }

    @Override
    public int getScore(Visitor visitor) {
        return visitor.getScore(this);
    }

    public BlueBug getShip(Visitor visitor) {
        return visitor.getShip(this);
    }

    public EnemyShip getEnemyShip(Visitor visitor) {
        return visitor.getShip(this);
    }

    @Override
    public void setReady(Visitor visitor, boolean r) {
        visitor.setReady(this, r);
    }

    @Override
    public void generateAttackPattern(Visitor visitor, Point screenSize, GridPattern gridPattern, PointF t) {
        visitor.generateAttackPattern(this, screenSize, gridPattern, t);
    }

    @Override
    public void executeAttackPattern(Visitor visitor, ArrayList<Bullet> playerShots, PlayerShip playerShip, long delta, Point screenSize, GridPattern gridPattern, Map<String, Bitmap> spritesheet) {

    }

    @Override
    public Effect getExplosion(Visitor visitor) {
        return visitor.getExplosion(this);
    }

    @Override
    public YellowBug getYellow(Visitor visitor) {
        return null;
    }

    @Override
    public RedBug getRed(Visitor visitor) {
        return null;
    }

    @Override
    public BlueBug getBlue(Visitor visitor) {
        return visitor.getShip(this);
    }

    @Override
    public Commander getCommander(Visitor visitor) {
        return null;
    }

    public void startFlying(Visitor visitor) {
        visitor.startFlying(this);
    }

    public float getDelay(Visitor visitor) {
        return visitor.getDelay(this);
    }

    public boolean isGridMode(Visitor visitor) {
        return visitor.isGridMode(this);
    }

    @Override
    public void setGridMode(Visitor visitor, boolean gm) {
        visitor.setGridMode(this, gm);
    }

    @Override
    public boolean isFinished(Visitor visitor) {
        return visitor.isFinished(this);
    }

    @Override
    public void update(Visitor visitor, ArrayList<Bullet> bullets, PlayerShip ship, long delta) {
        visitor.update(this, bullets, ship, delta);
    }

    @Override
    public ArrayList<Bullet> getBullets(Visitor visitor) {
        return visitor.getBullets(this);
    }

    @Override
    public int getHP(Visitor visitor) {
        return visitor.getHP(this);
    }

    @Override
    public int getShotChance(Visitor visitor) {
        return visitor.getShotChance(this);
    }

    @Override
    public PointF getPos(Visitor visitor) {
        return visitor.getPos(this);
    }

    @Override
    public void setPos(Visitor visitor, PointF p) {
        visitor.setPos(this, p);
    }

    @Override
    public int[] getGridPos(Visitor visitor) {
        return visitor.getGridPos(this);
    }

    public void setGridPos(Visitor visitor, Point p) {
        visitor.setGridPos(this, p);
    }

    @Override
    public void fire(Visitor visitor) {
        visitor.fire(this);
    }

    @Override
    public void addPath(Visitor visitor, AttackPattern p) {
        visitor.addPath(this, p);
    }

    @Override
    public enemyType getEnemyType(Visitor visitor) {
        return visitor.getEnemyType(this);
    }

    @Override
    public void drawRect(Visitor visitor, Paint p, Canvas c) {
        visitor.drawRect(this, p, c);
    }
}
