package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Rect;
import android.graphics.RectF;

/**
 * <h1>Game Object</h1>
 * Basic Game Object containing simple geometry
 * @author Kieran Clare
 */

public abstract class GameObject {
    private float width;
    private float height;
    public float x;
    public float y;
    public RectF rect;

    /**
     * <h2>Constructor</h2>
     * @param widthIn
     * @param heightIn
     * @param xIn
     * @param yIn
     */
    public GameObject(float widthIn, float heightIn, float xIn, float yIn){
        width = widthIn;
        height = heightIn;
        x = xIn;
        y = yIn;
        rect = new RectF(x, y, x + width, y + height);
    }

    /**
     * <h2>Set Width</h2>
     * @param widthIn
     */
    public void setWidth(float widthIn){
        width = widthIn;
    }

    /**
     * <h2>Set Height</h2>
     * @param heightIn
     */
    public void setHeight(float heightIn){
        height = heightIn;
    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
    }

    /**
     * <h2>Get Width</h2>
     * @return
     */
    public float getWidth(){
        return width;
    }

    /**
     * <h2>Get Height</h2>
     * @return
     */
    public float getHeight() {
        return height;
    }

    public boolean circleCollision(Bullet other){
        float a = (x - width) - (other.x - other.getWidth());
        float b = (y - width) - (other.y - other.getWidth());
        double distance = Math.sqrt((a*a)+(b*b));
        return distance < width + other.getWidth();
    }

    public boolean rectCollision(Ship other){
        if(Math.abs(x - other.x) < getWidth() + other.getWidth())
        {
            if(Math.abs(y - other.y) < getHeight() + other.getHeight())
            {
                return true;
            }
        }
        return false;
    }

    public boolean circleRectCollision(Bullet ball, Ship block){
        float extentsx = (block.getWidth()*0.5f);
        float extentsy = (block.getHeight()*0.5f);
        float diffx = ball.x - (block.x + extentsx);
        float diffy = ball.y - (block.y + extentsy);
        float diffconstrainedx = diffx;
        float diffconstrainedy = diffy;
        if(diffx > extentsx)
            diffconstrainedx = extentsx;
        else if(diffx < -extentsx)
            diffconstrainedx = -extentsx;
        if(diffy > extentsy)
            diffconstrainedy = extentsy;
        else if(diffy < -extentsy)
            diffconstrainedy = -extentsy;

        float collisionCheckx = diffx - diffconstrainedx;
        float collisionChecky = diffy - diffconstrainedy;
        float CCmag = (collisionCheckx*collisionCheckx)+(collisionChecky*collisionChecky);
        float distance = CCmag - (ball.getWidth()*ball.getWidth());
        if(distance < 0){
            return true;
        }
        return distance < 0;
    }

    public boolean circleRectCollision(Bullet ball, Rect block){
        float extentsx = (block.width()*0.5f);
        float extentsy = (block.height()*0.5f);
        float diffx = ball.x - block.centerX();
        float diffy = ball.y - block.centerY();
        float diffconstrainedx = diffx;
        float diffconstrainedy = diffy;
        if(diffx > extentsx)
            diffconstrainedx = extentsx;
        else if(diffx < -extentsx)
            diffconstrainedx = -extentsx;
        if(diffy > extentsy)
            diffconstrainedy = extentsy;
        else if(diffy < -extentsy)
            diffconstrainedy = -extentsy;

        float collisionCheckx = diffx - diffconstrainedx;
        float collisionChecky = diffy - diffconstrainedy;
        float CCmag = (collisionCheckx*collisionCheckx)+(collisionChecky*collisionChecky);
        float distance = CCmag - (ball.getWidth()*ball.getWidth());
        if(distance < 0){
            return true;
        }
        return distance < 0;
    }

   /* float circleDistancex = Math.abs(ball.x - (block.x + (block.getWidth()*0.5f)));
    float circleDistancey = Math.abs(ball.y - (block.y + (block.getHeight()*0.5f)));

        if (circleDistancex > (block.getWidth()*0.5f + ball.getWidth())) { return false; }
        if (circleDistancey > (block.getHeight()*0.5f + ball.getWidth())) { return false; }

        if (circleDistancex <= (block.getWidth()*0.5f)) { return true; }
        if (circleDistancey <= (block.getHeight()*0.5f)) { return true; }

    float cornerDistance_sq = ((circleDistancex - (block.getWidth()*0.5f))*(circleDistancex - (block.getWidth()*0.5f))) +
            ((circleDistancey - (block.getHeight()*0.5f))*(circleDistancey - (block.getHeight()*0.5f)));

        return (cornerDistance_sq <= (ball.getWidth()*ball.getWidth()));*/

}
