package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by p16163779 on 26/02/2018.
 */

public interface Visitable {

    public YellowBug getYellow(Visitor visitor);
    public RedBug getRed(Visitor visitor);
    public BlueBug getBlue(Visitor visitor);
    public Commander getCommander(Visitor visitor);

    public void startFlying(Visitor visitor);

    public float getDelay(Visitor visitor);

    public boolean isGridMode(Visitor visitor);

    public void setGridMode(Visitor visitor, boolean gm);

    public boolean isFinished(Visitor visitor);

    public void update(Visitor visitor, ArrayList<Bullet> bullets, PlayerShip ship, long delta);

    public ArrayList<Bullet> getBullets(Visitor visitor);

    public int getHP(Visitor visitor);

    public int getShotChance(Visitor visitor);

    public PointF getPos(Visitor visitor);

    public void setPos(Visitor visitor, PointF p);

    public int[] getGridPos(Visitor visitor);

    public void setGridPos(Visitor visitor, Point p);

    public void fire(Visitor visitor);

    public void addPath(Visitor visitor, AttackPattern p);

    public EnemyShip.enemyType getEnemyType(Visitor visitor);

    public void drawRect(Visitor visitor, Paint p, Canvas c);

    EnemyShip getEnemyShip(Visitor Visitor);

    public void setReady(Visitor visitor, boolean r);

    void generateAttackPattern(Visitor visitor, Point screenSize, GridPattern gridPattern, PointF t);

    void executeAttackPattern(Visitor visitor, ArrayList<Bullet> playerShots, PlayerShip playerShip, long delta,
                              Point screenSize, GridPattern gridPattern, Map<String, Bitmap> spritesheet);

    Effect getExplosion(Visitor visitor);

    int getScore(Visitor visitor);

}
