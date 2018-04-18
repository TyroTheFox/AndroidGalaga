package com.kieranclare.p16163779.galagalaxian.model;

import android.support.annotation.NonNull;

/**
 * Project: Galagalaxian
 * Created by p16163779 on 07/03/2018.
 */

public class Score implements Comparable {

    private String name;
    private int score;

    public Score(String name, int score){
        this.name = name;
        this.score = score;
    }

    public String getName(){
        return name;
    }

    public void setName(String n){
        name = n;
    }

    public int getScore(){
        return score;
    }

    public void setScore(int s){
        score = s;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Score sO = (Score) o;
        int returnScore = sO.getScore() - this.score;
        if(returnScore == 0){
            returnScore = sO.getScore() - 1;
        }
        return returnScore;
    }
}
