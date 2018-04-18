package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Point;

/**
 * Created by p16163779 on 24/02/2018.
 */

public class CenteredGrid extends GameObject {
    private int width, height;
    private float gridWidth, gridHeight;
    private float marginX = 10, marginY = 20;
    private int[] rowHeights;

    private boolean shuffleMode = true;
    private boolean shuffleOut = true;
    private float shuffleMax = 20, shuffleStep = 0.05f, shuffle = 0, centerX = 0;

    CenteredGrid(float x, float y, float objectWidth, float objectHeight, int width, int height){
        super(objectWidth, objectHeight, x - (objectWidth*0.5f), y - (objectWidth*0.5f));
        centerX = x;
        this.width = width;
        this.height = height;
        gridWidth = this.width * getWidth();
        gridHeight = this.height * getHeight();
        rowHeights = new int[4];
    }

    public void update(Point screenSize){
        boolean end;
        if(shuffleMode){
            end = shuffleOut(screenSize);
            if(end){
                shuffleMode = !shuffleMode;
            }
        }else{
            end = shuffleOut(screenSize);
            if(end){
                shuffleMode = !shuffleMode;
            }
        }

    }

    private boolean shuffleOut(Point screenSize){
        boolean end = false;
        float maxX = width+2;
        float right = (getWidth() * maxX) + ((marginX + shuffle) * maxX);
        if(right > screenSize.x){
            shuffleOut = false;
        } else if(shuffle <= 0){
            shuffleOut = true;
            end = true;
        }

        if(shuffleOut){
            shuffle += shuffleStep;
        }else{
            shuffle -= shuffleStep;
        }
        return end;
    }

    private boolean shuffleSideways(Point screenSize){
        boolean end = false;
        if(x + (gridWidth*0.5f) >= screenSize.x){
            shuffleOut = false;
        }else if(x - (gridWidth*0.5f) <= 0){
            shuffleOut = true;
            end = true;
        }

        if(shuffleOut){
            x += shuffleStep*10;
        }else{
            x -= shuffleStep*10;
        }

        boolean returned = false;
        if(end){
            if(x >= centerX){
                x = centerX;
                returned = true;
            }
        }
        return returned;
    }

    void addYellowHeight(int yellow){
        rowHeights[0] = yellow;
    }

    void addRedHeight(int red){
        rowHeights[1] = red;
    }

    void addBlueHeight(int blue){
        rowHeights[2] = blue;
    }

    void addCommanderHeight(int commander){
        rowHeights[3] = commander;
    }

    float[] getPosition(int x, int y, int enemyType){
        float[] temp = new float[2];
        if(x > 0) {
            float offset = 0;
            if ((x & 1) == 0) {
                //Even
                offset += (getWidth() * (x * 0.5f)) + ((marginX + shuffle) * x);
            } else {
                //Odd
                offset -= (getWidth() * (x * 0.5f)) + ((marginX + shuffle) * x);
            }
            if(offset < 0){offset -= (getWidth() * 0.75f);}
            temp[0] = this.x + offset;
        }else{
            temp[0] = this.x;
        }

        float rowHeight = 0;
        switch (enemyType) {
            case 0:
                rowHeight = height;
                break;
            case 1:
                rowHeight = (rowHeights[1] + rowHeights[2] + rowHeights[3]) + 3;
                break;
            case 2:
                rowHeight = (rowHeights[2] + rowHeights[3]) + 2;
                break;
            case 3:
                rowHeight = (rowHeights[3]) + 1;
                break;
        }

        temp[1] =  this.y + (getHeight() * rowHeight) + (getHeight() * y) + (marginY * y) + (marginY * rowHeight);

        return temp;
    }
}
