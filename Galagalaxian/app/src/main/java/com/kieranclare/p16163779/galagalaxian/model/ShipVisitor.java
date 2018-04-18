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

public class ShipVisitor implements Visitor {
    @Override
    public void startFlying(YellowBug yellowBug) {
        yellowBug.startFlying();
    }
    @Override
    public void startFlying(RedBug redBug) {
        redBug.startFlying();
    }
    @Override
    public void startFlying(BlueBug blueBug) {
        blueBug.startFlying();
    }

    @Override
    public void startFlying(Commander commander) {
        commander.startFlying();
    }
    @Override
    public float getDelay(YellowBug yellowBug) {
        return yellowBug.delay;
    }
    @Override
    public float getDelay(RedBug redBug) {
        return redBug.delay;
    }
    @Override
    public float getDelay(BlueBug blueBug) {
        return blueBug.delay;
    }

    @Override
    public float getDelay(Commander commander) {
        return commander.delay;
    }
    @Override
    public boolean isGridMode(YellowBug yellowBug) {
        return yellowBug.gridMode;
    }
    @Override
    public boolean isGridMode(RedBug redBug) {
        return redBug.gridMode;
    }
    @Override
    public boolean isGridMode(BlueBug blueBug) {
        return blueBug.gridMode;
    }
    @Override
    public boolean isGridMode(Commander commander) {
        return commander.gridMode;
    }

    @Override
    public void setGridMode(YellowBug yellowBug, boolean gm) {
        yellowBug.gridMode = gm;
    }

    @Override
    public void setGridMode(RedBug redBug, boolean gm) {
        redBug.gridMode = gm;
    }

    @Override
    public void setGridMode(BlueBug blueBug, boolean gm) {
        blueBug.gridMode = gm;
    }

    @Override
    public void setGridMode(Commander commander, boolean gm) {
        commander.gridMode = gm;
    }

    @Override
    public void setReady(YellowBug yellowBug, boolean r) {
        yellowBug.ready = r;
    }

    @Override
    public void setReady(RedBug redBug, boolean r) {
        redBug.ready = r;
    }

    @Override
    public void setReady(BlueBug blueBug, boolean r) {
        blueBug.ready = r;
    }

    @Override
    public void setReady(Commander commander, boolean r) {
        commander.ready = r;
    }

    @Override
    public boolean isFinished(YellowBug yellowBug) {
        return yellowBug.finished;
    }

    @Override
    public boolean isFinished(RedBug redBug) {
        return redBug.finished;
    }

    @Override
    public boolean isFinished(BlueBug blueBug) {
        return blueBug.finished;
    }

    @Override
    public boolean isFinished(Commander commander) {
        return commander.finished;
    }

    @Override
    public void update(YellowBug yellowBug, ArrayList<Bullet> bullets, PlayerShip ship, long delta) {
        yellowBug.update(bullets, ship, delta);
    }
    @Override
    public void update(RedBug redBug, ArrayList<Bullet> bullets, PlayerShip ship, long delta) {
        redBug.update(bullets, ship, delta);
    }
    @Override
    public void update(BlueBug blueBug, ArrayList<Bullet> bullets, PlayerShip ship, long delta) {
        blueBug.update(bullets, ship, delta);
    }
    @Override
    public void update(Commander commander, ArrayList<Bullet> bullets, PlayerShip ship, long delta) {
        commander.update(bullets, ship, delta);
    }

    @Override
    public ArrayList<Bullet> getBullets(YellowBug yellowBug) {
        return yellowBug.getAllBullets();
    }
    @Override
    public ArrayList<Bullet> getBullets(RedBug redBug) {
        return redBug.getAllBullets();
    }

    @Override
    public ArrayList<Bullet> getBullets(BlueBug blueBug) {
        return blueBug.getAllBullets();
    }

    @Override
    public ArrayList<Bullet> getBullets(Commander commander) {
        return commander.getAllBullets();
    }

    @Override
    public int getHP(YellowBug yellowBug) {
        return yellowBug.HP;
    }

    @Override
    public int getHP(RedBug redBug) {
        return redBug.HP;
    }

    @Override
    public int getHP(BlueBug blueBug) {
        return blueBug.HP;
    }

    @Override
    public int getHP(Commander commander) {
        return commander.HP;
    }

    @Override
    public int getShotChance(YellowBug yellowBug) {
        return yellowBug.shotChance;
    }

    @Override
    public int getShotChance(RedBug redBug) {
        return redBug.shotChance;
    }

    @Override
    public int getShotChance(BlueBug blueBug) {
        return blueBug.shotChance;
    }

    @Override
    public int getShotChance(Commander commander) {
        return commander.shotChance;
    }

    @Override
    public PointF getPos(YellowBug yellowBug) {
        return new PointF(yellowBug.x, yellowBug.y);
    }

    @Override
    public PointF getPos(RedBug redBug) {
        return new PointF(redBug.x, redBug.y);
    }

    @Override
    public PointF getPos(BlueBug blueBug) {
        return new PointF(blueBug.x, blueBug.y);
    }

    @Override
    public PointF getPos(Commander commander) {
        return new PointF(commander.x, commander.y);
    }

    @Override
    public void setPos(YellowBug yellowBug, PointF p) {
        yellowBug.x = p.x;
        yellowBug.y = p.y;
    }

    @Override
    public void setPos(RedBug redBug, PointF p) {
        redBug.x = p.x;
        redBug.y = p.y;
    }

    @Override
    public void setPos(BlueBug blueBug, PointF p) {
        blueBug.x = p.x;
        blueBug.y = p.y;
    }

    @Override
    public void setPos(Commander commander, PointF p) {
        commander.x = p.x;
        commander.y = p.y;
    }

    @Override
    public int[] getGridPos(YellowBug yellowBug) {
        return new int[]{yellowBug.gridx, yellowBug.gridy};
    }

    @Override
    public int[] getGridPos(RedBug redBug) {
        return new int[]{redBug.gridx, redBug.gridy};
    }

    @Override
    public int[] getGridPos(BlueBug blueBug) {
        return new int[]{blueBug.gridx, blueBug.gridy};
    }

    @Override
    public int[] getGridPos(Commander commander) {
        return new int[]{commander.gridx, commander.gridy};
    }

    public void setGridPos(YellowBug yellowBug, Point p) {
        yellowBug.gridx = p.x;
        yellowBug.gridy = p.y;
    }

    public void setGridPos(RedBug redBug, Point p) {
        redBug.gridx = p.x;
        redBug.gridy = p.y;
    }

    public void setGridPos(BlueBug blueBug, Point p) {
        blueBug.gridx = p.x;
        blueBug.gridy = p.y;
    }

    public void setGridPos(Commander commander, Point p) {
        commander.gridx = p.x;
        commander.gridy = p.y;
    }

    @Override
    public void fire(YellowBug yellowBug) {
        yellowBug.fire();
    }

    @Override
    public void fire(RedBug redBug) {
        redBug.fire();
    }

    @Override
    public void fire(BlueBug blueBug) {
        blueBug.fire();
    }

    @Override
    public void fire(Commander commander) {
        commander.fire();
    }

    @Override
    public void addPath(YellowBug yellowBug, AttackPattern p) {
        yellowBug.addPath(p);
    }

    @Override
    public void addPath(BlueBug blueBug, AttackPattern p) {
        blueBug.addPath(p);
    }

    @Override
    public void addPath(RedBug redBug, AttackPattern p) {
        redBug.addPath(p);
    }

    @Override
    public void addPath(Commander commander, AttackPattern p) {
        commander.addPath(p);
    }

    @Override
    public EnemyShip.enemyType getEnemyType(YellowBug yellowBug) {
        return yellowBug.eType;
    }

    @Override
    public EnemyShip.enemyType getEnemyType(RedBug redBug) {
        return redBug.eType;
    }

    @Override
    public EnemyShip.enemyType getEnemyType(BlueBug blueBug) {
        return blueBug.eType;
    }

    @Override
    public EnemyShip.enemyType getEnemyType(Commander commander) {
        return commander.eType;
    }

    @Override
    public YellowBug getShip(YellowBug yellowBug) {
        return yellowBug;
    }

    @Override
    public RedBug getShip(RedBug redBug) {
        return redBug;
    }

    @Override
    public BlueBug getShip(BlueBug blueBug) {
        return blueBug;
    }

    @Override
    public Commander getShip(Commander commander) {
        return commander;
    }

    @Override
    public EnemyShip getShip(EnemyShip enemyShip) {
        return enemyShip;
    }

    @Override
    public void drawRect(YellowBug yellowBug, Paint p, Canvas c) {
        yellowBug.drawRect(p, c);
    }

    @Override
    public void drawRect(RedBug redBug, Paint p, Canvas c) {
        redBug.drawRect(p, c);
    }

    @Override
    public void drawRect(BlueBug blueBug, Paint p, Canvas c) {
        blueBug.drawRect(p, c);
    }

    @Override
    public void drawRect(Commander commander, Paint p, Canvas c) {
        commander.drawRect(p, c);
    }

    @Override
    public void generateAttackPattern(YellowBug yellowBug, Point screenSize, GridPattern gridPattern, PointF t) {
        yellowBug.generateUniqueAttackPattern(screenSize, gridPattern, t);
    }

    @Override
    public void generateAttackPattern(RedBug redBug, Point screenSize, GridPattern gridPattern, PointF t) {
        redBug.generateUniqueAttackPattern(screenSize, gridPattern, t);
    }

    @Override
    public void generateAttackPattern(BlueBug blueBug, Point screenSize, GridPattern gridPattern, PointF t) {
        blueBug.generateUniqueAttackPattern(screenSize, gridPattern, t);
    }

    @Override
    public void executeAttackPattern(Commander commander, ArrayList<Bullet> playerShots, PlayerShip playerShip, long delta, Point screenSize, GridPattern gridPattern, Map<String, Bitmap> spritesheet) {
        commander.commanderAttack(playerShots, playerShip, delta, screenSize, gridPattern, spritesheet);
    }

    @Override
    public Effect getExplosion(YellowBug yellowBug) {
        return yellowBug.getEffect();
    }

    @Override
    public Effect getExplosion(RedBug redBug) {
        return redBug.getEffect();
    }

    @Override
    public Effect getExplosion(BlueBug blueBug) {
        return blueBug.getEffect();
    }

    @Override
    public Effect getExplosion(Commander commander) {
        return commander.getEffect();
    }

    @Override
    public int getScore(YellowBug yellowBug) {
        return yellowBug.score;
    }

    @Override
    public int getScore(RedBug redBug) {
        return redBug.score;
    }

    @Override
    public int getScore(BlueBug blueBug) {
        return blueBug.score;
    }

    @Override
    public int getScore(Commander commander) {
        return commander.score;
    }
}
