package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Color;

/**
 * Created by p16163779 on 13/02/2018.
 */

public class BlueBug extends EnemyShip {
    int weight = 2;
    /**
     * @param widthIn
     * @param heightIn
     * @param xIn
     * @param yIn
     * @param speed
     */
    public BlueBug(float widthIn, float heightIn, float xIn, float yIn, float speed) {
        super(widthIn, heightIn, xIn, yIn, Color.BLUE, speed);
        eType = enemyType.BLUE;
    }
}
