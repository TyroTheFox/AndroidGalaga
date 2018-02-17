package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Color;

/**
 * Created by p16163779 on 13/02/2018.
 */

public class Commander extends EnemyShip {
    int weight = 1;
    /**
     * @param widthIn
     * @param heightIn
     * @param xIn
     * @param yIn
     * @param speed
     */
    public Commander(float widthIn, float heightIn, float xIn, float yIn, float speed) {
        super(widthIn, heightIn, xIn, yIn, Color.CYAN, speed);
        eType = enemyType.COMMANDER;
    }
}
