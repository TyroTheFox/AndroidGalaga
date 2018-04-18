package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by p16163779 on 16/02/2018.
 */

public class AttackGroup extends EnemyGroup {
    SoundEffects soundEffects;

    public AttackGroup(float delay, Point screenS){
        super(delay, screenS);
    }

    public void setSoundEffects(SoundEffects soundEffectsObject){
        soundEffects = soundEffectsObject;
    }

    public ArrayList<Bullet> updateAll(ArrayList<Bullet> playerShots, PlayerShip playerShip, long delta) {
        int activeShipsAtStart = activeShips.size();
        ArrayList<Bullet> temp = super.updateAll(playerShots, playerShip, delta);

        if (soundEffects != null){
            if (activeShipsAtStart < activeShips.size()) {
                soundEffects.play("galaga_dive", 0.8f);
            }
        }

        return temp;
    }
}
