package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PathMeasure;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by p16163779 on 07/02/2018.
 */

public class EnemyShip extends Ship {
    public float speed = 1;
    public boolean ready = true, firing = false, attacking = false;
    public float fireRate = 10000, tickSize = 0.1f, time = 0;
    public float delay = 0, tick = 0.01f, flyTime = 0;
    public int MaxHP = 1, HP = MaxHP;
    public int gridx = 0, gridy = 0;
    enum enemyType {YELLOW, RED, BLUE, COMMANDER};
    enemyType eType;
    public int shotChance = 5;

    float rotation = 0;

    public boolean dead = false;

    private LinkedList<AttackPattern> flightPath;
    PathMeasure pathMeasure;
    AttackPattern currentPath;
    private int pathOrderPos = 0;
    float distance;
    float[] pos;
    float[] tan;
    float pathLength;
    public boolean pathReady = true, following = false, flying = false, finished = true, gridMode = false;

    public boolean hurt = false;

    /**
     *
     * @param widthIn
     * @param heightIn
     * @param xIn
     * @param yIn
     * @param colour
     * @param speed
     */
    public EnemyShip(float widthIn, float heightIn, float xIn, float yIn, int colour, float speed) {
        super(widthIn, heightIn, xIn, yIn, colour);
        this.speed = speed;
        rotation = 0;
        pathMeasure = new PathMeasure();
        distance = 0;
        pos = new float[2];
        tan = new float[2];
        pathLength = 0;
        flightPath = new LinkedList<>();
    }

    public void move(float inX, float inY){
        x = lerp(x, inX, speed);
        y = lerp(y, inY, speed);
    }

    @Override
    public void fire(){
        if(ready) {
            bulletBank.fireNew(x + (getWidth()*0.5f), y + getHeight(), 0, 25, damageMultiplier);
            ready = false;
            firing = true;
        }
    }

    public void BulletCollisionCheck(ArrayList<Bullet> bullets){
        hurt = false;
        for (Bullet bullet : bullets){
            if(circleRectCollision(bullet, this)){
                damage(bullet.damage);
                bullet.active = false;
                bullet.hitLanded = true;
            }
        }
    }

    public void ShipCollisionCheck(PlayerShip ship){
        hurt = false;
        if(rectCollision(ship.rect)) {
            damage(1);
        }
    }

    private void damage(int amount){
        if(hurt){
            return;
        }
        HP -= amount;
        hurt = true;
    }

    public void update(ArrayList<Bullet> bullets, PlayerShip ship, long delta){
        if(HP > 0) {
            //updateVector(delta);
            //Vector2 v = new Vector2(ship.x, ship.y);
            //seek(v);
            rect.set(x, y, x + getWidth(), y + getHeight());
            if(flightPath.size() > 0 && !finished){
                if(!following) {
                    flyTime += delta * tick;
                    if (flyTime >= delay) {
                        continuousFlight();
                        following = true;
                        flyTime = 0;
                    }
                }
            }else{
                //currentPath = null;
                flying = false;

            }

            if(following){
                followPath();
            }
            BulletCollisionCheck(bullets);
            if(ship.HP > 0) {
                ShipCollisionCheck(ship);
            }
        }
        if(gridMode){
            rotation = -90;
        }
            bulletBank.updateAll();
        if(HP > 0) {
            if (firing) {
                time += delta * tickSize;
                if (time >= fireRate) {
                    firing = false;
                    ready = true;
                    time = 0;
                }
            }
        }
    }

    @Override
    public void drawRect(Paint p, Canvas c){
        bulletBank.drawAll(p, c);
    }

    public void addPath(AttackPattern p){
        flightPath.add(p);
    }

    public void clearPaths(){
        flightPath.clear();
    }

    public void startFlying(){
        flying = true;
        attacking = true;
        finished = false;
        pathReady = true;
        following = false;
        gridMode = false;
        pathOrderPos = 0;
    }

    public void setNewPath(AttackPattern p){
        pathMeasure.setPath(p, false);
        pathLength = pathMeasure.getLength();
        currentPath = p;
        distance = 0;
    }

    public void continuousFlight(){
        if(flightPath.size() > pathOrderPos) {
            currentPath = flightPath.pop();
            pathMeasure.setPath(currentPath, false);
            pathLength = pathMeasure.getLength();
            distance = 0;
        }
    }

    public void followPath(){
        pathReady = false;
        if(distance < pathLength){
            pathMeasure.getPosTan(distance, pos, tan);
            x = pos[0] - getWidth()*0.5f;
            y = pos[1] - getHeight()*0.5f;
            //matrix.reset();
            rotation = (float)(Math.atan2(tan[1], tan[0])*180.0/Math.PI);
            //matrix.postRotate(degrees, bm_offsetX, bm_offsetY);
            //matrix.postTranslate(pos[0]-bm_offsetX, pos[1]-bm_offsetY);

            //canvas.drawBitmap(bm, matrix, null);

            distance += speed;
        }else{
            pathMeasure.getPosTan(pathLength, pos, tan);
            x = pos[0] - getWidth()*0.5f;
            y = pos[1] - getHeight()*0.5f;
            //matrix.reset();
            rotation = (float)(Math.atan2(tan[1], tan[0])*180.0/Math.PI);
            pathOrderPos++;
            pathReady = true;
            following = false;
            finished = true;
            attacking = false;
            currentPath = null;
        }
    }
}
