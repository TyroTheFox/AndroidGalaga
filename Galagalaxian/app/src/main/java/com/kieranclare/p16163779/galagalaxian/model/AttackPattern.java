package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Path;

/**
 * Created by p16163779 on 09/02/2018.
 */

public class AttackPattern extends Path {
    public float delay = 1;
    public int attackName = 0;
    public AttackPattern(){
        super();
    }
    public AttackPattern(float delay){
        super();
        this.delay = delay;
    }
}
