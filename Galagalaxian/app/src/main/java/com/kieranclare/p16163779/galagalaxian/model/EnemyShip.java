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
    public boolean ready = true, firing = false;
    public float fireRate = 7, tickSize = 0.1f, time = 0;
    public float delay = 0, tick = 0.01f, flyTime = 0;
    public int MaxHP = 1, HP = MaxHP;
    public int gridx = 0, gridy = 0;
    enum enemyType {YELLOW, RED, BLUE, COMMANDER};
    enemyType eType;

    private float rotation;

    private LinkedList<AttackPattern> flightPath;
    PathMeasure pathMeasure;
    AttackPattern currentPath;
    private int pathOrderPos = 0;
    float distance;
    float[] pos;
    float[] tan;
    float pathLength;
    public boolean pathReady = true, following = false, flying = false;

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
            bulletBank.fireNew(x + (getWidth()*0.5f), y + getHeight(), 0, 10, damageMultiplier);
            ready = false;
            firing = true;
        }
    }

    public void BulletCollisionCheck(ArrayList<Bullet> bullets){
        for (Bullet bullet : bullets){
            if(circleRectCollision(bullet, this)){
                HP--;
                bullet.life = 0;
            }
        }
    }

    public void ShipCollisionCheck(PlayerShip ship){
        if(rectCollision(ship)) {
            HP--;
        }
    }

    public void update(ArrayList<Bullet> bullets, PlayerShip ship, long delta){
        if(HP > 0) {
            //updateVector(delta);
            //Vector2 v = new Vector2(ship.x, ship.y);
            //seek(v);
            if(flightPath.size() > 0 && flying){
                if(!following) {
                    flyTime += delta * tick;
                    if (flyTime >= delay) {
                        continuousFlight();
                        following = true;
                        flyTime = 0;
                    }
                }
            }else{
                currentPath = null;
                flying = false;
            }

            if(following){
                followPath();
            }
            /*
            if(flying) {
                if (pathReady) {
                    distance = 0;
                    continuousFlight();
                    if(currentPath != null) {
                        delay = currentPath.delay;
                    }
                }
                flyTime += delta * tick;
                if (flyTime >= delay) {
                    following = true;
                    flyTime = 0;
                }
                if(following) {
                    followPath();
                }
            }*/
            //rect.set(x, y, x + getWidth(), y + getHeight());
            BulletCollisionCheck(bullets);
            //ShipCollisionCheck(ship);
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
        //c.drawPath(currentPath, p);
        if(HP > 0) {
            p.setColor(colour);
            c.save();
            //c.rotate(rotation, x - getWidth(), y - getHeight());
            c.drawRect(x, y, x + getWidth(), y + getHeight(), p);
            c.restore();
        }
        bulletBank.drawAll(p, c);
    }

    /*public void updateVector(long delta){
        if(delta == 0){
            velocity.set(0, 0);
            return;
        }
        velocity.set((float)(x/delta), (float)(y/delta));
    }*/

    public void addPath(AttackPattern p){
        flightPath.add(p);
    }

    public void clearPaths(){
        flightPath.clear();
    }

    public void startFlying(){
        flying = true;
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
        }
    }

    /*public void seek(Vector2 pTarget){
        Vector2 vecDesired;
        // 1. vector(desired velocity) = (target position) - (vehicle position)
        vecDesired = pTarget.sub(new Vector2(x, y));

        // 2. normalize vector(desired velocity)
        vecDesired.normalize();

        // 3. scale vector(desired velocity) to maximum speed
        vecDesired.multiply(MAX_SPEED);

        // 4. vector(steering force) = vector(desired velocity) - vector(current velocity)
        Vector2 vecSteer = vecDesired.sub(velocity);

        // 5. limit the magnitude of vector(steering force) to maximum force
        if (vecSteer.len2() > MAX_STEER_SQ) {
            vecSteer.setMagnitude(MAX_STEER);
        }

        // 6. vector(new velocity) = vector(current velocity) + vector(steering force)
        velocity.add(vecSteer);

        x += velocity.x;
        y += velocity.y;

        if(pTarget.x == x && pTarget.y == y){
            return;
        }

        // 7. limit the magnitude of vector(new velocity) to maximum speed
        if (velocity.len2() > MAX_SPEED_SQ) {
           velocity.setMagnitude(MAX_SPEED);
        }

        // 8. update vehicle rotation according to the angle of the vehicle velocity
        vecReference.add(velocity);
        rotation = (float)Math.atan((double)(vecReference.x/vecReference.y));
    }*/
}
