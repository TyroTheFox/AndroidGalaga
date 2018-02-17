package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Color;

/**
 * Created by p16163779 on 13/02/2018.
 */

public class RedBug extends EnemyShip {
    int weight = 3;
    /**
     * @param widthIn
     * @param heightIn
     * @param xIn
     * @param yIn
     * @param speed
     */
    public RedBug(float widthIn, float heightIn, float xIn, float yIn, float speed) {
        super(widthIn, heightIn, xIn, yIn, Color.RED, speed);
        eType = enemyType.RED;
    }
}
