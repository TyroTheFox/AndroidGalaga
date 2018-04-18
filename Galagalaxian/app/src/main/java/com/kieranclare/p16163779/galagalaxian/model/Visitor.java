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

public interface Visitor {
    public EnemyShip.enemyType getEnemyType(YellowBug yellowBug);
    public EnemyShip.enemyType getEnemyType(RedBug redBug);
    public EnemyShip.enemyType getEnemyType(BlueBug blueBug);
    public EnemyShip.enemyType getEnemyType(Commander commander);

    public YellowBug getShip(YellowBug yellowBug);
    public RedBug getShip(RedBug redBug);
    public BlueBug getShip(BlueBug blueBug);
    public Commander getShip(Commander commander);
    public EnemyShip getShip(EnemyShip enemyShip);

    public void startFlying(YellowBug yellowBug);
    public void startFlying(RedBug redBug);
    public void startFlying(BlueBug blueBug);
    public void startFlying(Commander commander);

    public float getDelay(YellowBug yellowBug);
    public float getDelay(RedBug redBug);
    public float getDelay(BlueBug blueBug);
    public float getDelay(Commander commander);

    public boolean isGridMode(YellowBug yellowBug);
    public boolean isGridMode(RedBug redBug);
    public boolean isGridMode(BlueBug blueBug);
    public boolean isGridMode(Commander commander);

    public void setGridMode(YellowBug yellowBug, boolean gm);
    public void setGridMode(RedBug redBug, boolean gm);
    public void setGridMode(BlueBug blueBug, boolean gm);
    public void setGridMode(Commander commander, boolean gm);

    public void setReady(YellowBug yellowBug, boolean r);
    public void setReady(RedBug redBug, boolean r);
    public void setReady(BlueBug blueBug, boolean r);
    public void setReady(Commander commander, boolean r);

    public boolean isFinished(YellowBug yellowBug);
    public boolean isFinished(RedBug redBug);
    public boolean isFinished(BlueBug blueBug);
    public boolean isFinished(Commander commander);

    public void update (YellowBug yellowBug, ArrayList<Bullet> bullets, PlayerShip ship, long delta);
    public void update (RedBug redBug, ArrayList<Bullet> bullets, PlayerShip ship, long delta);
    public void update (BlueBug blueBug, ArrayList<Bullet> bullets, PlayerShip ship, long delta);
    public void update (Commander commander, ArrayList<Bullet> bullets, PlayerShip ship, long delta);

    public ArrayList<Bullet> getBullets(YellowBug yellowBug);
    public ArrayList<Bullet> getBullets(RedBug redBug);
    public ArrayList<Bullet> getBullets(BlueBug blueBug);
    public ArrayList<Bullet> getBullets(Commander commander);

    public int getHP(YellowBug yellowBug);
    public int getHP(RedBug redBug);
    public int getHP(BlueBug blueBug);
    public int getHP(Commander commander);

    public int getShotChance(YellowBug yellowBug);
    public int getShotChance(RedBug redBug);
    public int getShotChance(BlueBug blueBug);
    public int getShotChance(Commander commander);

    public PointF getPos(YellowBug yellowBug);
    public PointF getPos(RedBug redBug);
    public PointF getPos(BlueBug blueBug);
    public PointF getPos(Commander commander);

    public void setPos(YellowBug yellowBug, PointF p);
    public void setPos(RedBug redBug, PointF p);
    public void setPos(BlueBug blueBug, PointF p);
    public void setPos(Commander commander, PointF p);

    public int[] getGridPos(YellowBug yellowBug);
    public int[] getGridPos(RedBug redBug);
    public int[] getGridPos(BlueBug blueBug);
    public int[] getGridPos(Commander commander);

    public void setGridPos(YellowBug yellowBug, Point p);
    public void setGridPos(RedBug redBug, Point p);
    public void setGridPos(BlueBug blueBug, Point p);
    public void setGridPos(Commander commander, Point p);

    public void fire(YellowBug yellowBug);
    public void fire(RedBug redBug);
    public void fire(BlueBug blueBug);
    public void fire(Commander commander);

    public void addPath(YellowBug yellowBug, AttackPattern p);
    public void addPath(BlueBug blueBug, AttackPattern p);
    public void addPath(RedBug redBug, AttackPattern p);
    public void addPath(Commander commander, AttackPattern p);

    public void drawRect(YellowBug yellowBug, Paint p, Canvas c);
    public void drawRect(RedBug redBug, Paint p, Canvas c);
    public void drawRect(BlueBug blueBug, Paint p, Canvas c);
    public void drawRect(Commander commander, Paint p, Canvas c);

    void generateAttackPattern(YellowBug yellowBug, Point screenSize, GridPattern gridPattern, PointF t);
    void generateAttackPattern(RedBug redBug, Point screenSize, GridPattern gridPattern, PointF t);
    void generateAttackPattern(BlueBug blueBug, Point screenSize, GridPattern gridPattern, PointF t);
    void executeAttackPattern(Commander commander, ArrayList<Bullet> playerShots, PlayerShip playerShip, long delta,
                               Point screenSize, GridPattern gridPattern, Map<String, Bitmap> spritesheet);

    Effect getExplosion(YellowBug yellowBug);
    Effect getExplosion(RedBug redBug);
    Effect getExplosion(BlueBug blueBug);
    Effect getExplosion(Commander commander);

    int getScore(YellowBug yellowBug);
    int getScore(RedBug redBug);
    int getScore(BlueBug blueBug);
    int getScore(Commander commander);
}
