package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by p16163779 on 10/02/2018.
 */

public class GridPattern extends EnemyGroup{

    public float x = 0, y = 0, width = 0, height = 0;
    private float marginX = 10, marginY = 20;

    private EnemyShip positionsYellow[][], positionsRed[][], positionsBlue[][], positionsCom[][];
    //ArrayList<EnemyShip[][]> positions = new ArrayList();
    List<EnemyShip[][]> ships = new ArrayList<>();

    private int positionYellow = 0, currentRowYellow = 0, currentColumnYellow = 0;
    private int positionRed = 0, currentRowRed = 0, currentColumnRed = 0;
    private int positionBlue = 0, currentRowBlue = 0, currentColumnBlue = 0;
    private int positionCom = 0, currentRowCom = 0, currentColumnCom = 0;

    private int columns, rows, arrayLength;
    private int shipCount = 0;

    private Point screenSize;

    public int yellowCount = 0;
    public int redCount = 0;
    public int blueCount = 0;
    public int comCount = 0;

    enum enemyType {YELLOW, RED, BLUE, COMMANDER};

    /**
     *
     * @param columns
     * @param rows
     * @param xIn
     * @param yIn
     */
    public GridPattern(int columns, int rows, float xIn, float yIn, Point screenS){
        super();
        width = screenS.x ;
        height = screenS.y*0.5f;
        x = xIn;
        y = yIn;

        positionsYellow = new EnemyShip[columns][rows];
        positionsRed = new EnemyShip[columns][rows];
        positionsBlue = new EnemyShip[columns][rows];
        positionsCom = new EnemyShip[columns][rows];
        ships.add(positionsYellow);
        ships.add(positionsRed);
        ships.add(positionsBlue);
        ships.add(positionsCom);
        arrayLength = columns * rows;
        this.columns = columns;
        this.rows = rows;
        screenSize = screenS;
    }

    @Override
    public void addShip(EnemyShip enemyShip) {
        if(enemyShip.eType == EnemyShip.enemyType.YELLOW){
            addYellowBug(enemyShip);
        }
        if(enemyShip.eType == EnemyShip.enemyType.RED){
            addRedBug(enemyShip);
        }
        if(enemyShip.eType == EnemyShip.enemyType.BLUE){
            addBlueBug(enemyShip);
        }
        if(enemyShip.eType == EnemyShip.enemyType.COMMANDER){
            addCommanderBug(enemyShip);
        }
    }

    @Override
    public void addShips(ArrayList<EnemyShip> groupShips) {
        for(EnemyShip enemyShip : groupShips){
            if(enemyShip.eType == EnemyShip.enemyType.YELLOW){
                addYellowBug(enemyShip);
            }
            if(enemyShip.eType == EnemyShip.enemyType.RED){
                addRedBug(enemyShip);
            }
            if(enemyShip.eType == EnemyShip.enemyType.BLUE){
                addBlueBug(enemyShip);
            }
            if(enemyShip.eType == EnemyShip.enemyType.COMMANDER){
                addCommanderBug(enemyShip);
            }
        }
    }

    public boolean addYellowBug(EnemyShip enemyShip) {
        if(positionYellow >= arrayLength){
            return false;
        }
        if(currentRowYellow >= rows){
            return false;
        }
        if(currentColumnYellow >= columns -1){
            currentRowYellow++;
            currentColumnYellow = 0;
        }
        ships.get(0)[currentColumnYellow][currentRowYellow] = enemyShip;
        enemyShip.gridx = currentColumnYellow;
        enemyShip.gridy = currentRowYellow;
        currentColumnYellow++;
        positionYellow++;
        shipCount++;
        return true;
    }

    public boolean addRedBug(EnemyShip enemyShip) {
        if(positionRed >= arrayLength){
            return false;
        }
        if(currentRowRed >= rows){
            return false;
        }
        if(currentColumnRed >= columns - 1){
            currentRowRed++;
            currentColumnRed = 0;
        }
        ships.get(1)[currentColumnRed][currentRowRed] = enemyShip;
        enemyShip.gridx = currentColumnRed;
        enemyShip.gridy = currentRowRed;
        currentColumnRed++;
        positionRed++;
        shipCount++;
        return true;
    }

    public boolean addBlueBug(EnemyShip enemyShip) {
        if(positionBlue >= arrayLength){
            return false;
        }
        if(currentRowBlue >= rows){
            return false;
        }
        if(currentColumnBlue >= columns - 1){
            currentRowBlue++;
            currentColumnBlue = 0;
        }
        ships.get(2)[currentColumnBlue][currentRowBlue] = enemyShip;
        enemyShip.gridx = currentColumnBlue;
        enemyShip.gridy = currentRowBlue;
        currentColumnBlue++;
        positionBlue++;
        shipCount++;
        return true;
    }

    public boolean addCommanderBug(EnemyShip enemyShip) {
        if(positionCom >= arrayLength){
            return false;
        }
        if(currentRowCom >= rows){
            return false;
        }
        if(currentColumnCom >= columns - 1){
            currentRowCom++;
            currentColumnCom = 0;
        }
        ships.get(3)[currentColumnCom][currentRowCom] = enemyShip;
        enemyShip.gridx = currentColumnCom;
        enemyShip.gridy = currentRowCom;
        currentColumnCom++;
        positionCom++;
        shipCount++;
        return true;
    }

    public boolean isEmpty(){
        return ships.size() <= 0;
    }

    public EnemyShip getRandomShip(){
        Random random = new Random();
        ArrayList<EnemyShip> notFlying = new ArrayList();
        for(EnemyShip[][] shipType : ships){
            for(EnemyShip[] gridX : shipType){
                for(EnemyShip gridY : gridX){
                    if(gridY != null){
                        if(!gridY.flying) {
                            notFlying.add(gridY);
                        }
                    }
                }
            }
        }
        if(notFlying.size() == 0){
            return new EnemyShip(1, 1, 1, 1,1, 1);
        }
        EnemyShip temp = notFlying.get(random.nextInt(notFlying.size()));
        return temp;
    }

    public float[] getPosition(int gridX, int gridY, int enemyType){
        float screenCenter = 0;
        float shipWidth = 0, shipHeight = 0;
        screenCenter = (screenSize.x - (ships.get(enemyType)[gridX][gridY].getWidth()))*0.5f;
        shipWidth = ships.get(enemyType)[gridX][gridY].getWidth();
        shipHeight = ships.get(enemyType)[gridX][gridY].getHeight();

        float offset = 0;
        if( (gridX & 1) == 0 && gridX > 0) {offset += (shipWidth * (Math.round(gridX*0.5f))) + (marginX * gridX);}
        else {offset -= (shipWidth * (Math.round(gridX*0.5f))) + (marginX * gridX);}
        return new float[]{screenCenter + offset,
                y + (shipHeight * gridY) + (shipHeight * (3 - enemyType))
                        + (marginY * gridY) + (marginY * (3 - enemyType))};
    }

    public ArrayList<Bullet> updateAll(ArrayList<Bullet> playerShots, PlayerShip playerShip, long delta){
        ArrayList<Bullet> enemyShots = new ArrayList<>();
        ArrayList<EnemyShip> toRemove = new ArrayList<>();
        for(EnemyShip[][] shipType : ships){
            for(EnemyShip[] gridX : shipType){
                for(EnemyShip gridY : gridX){
                    if(gridY != null) {
                        gridY.update(playerShots, playerShip, delta);
                        enemyShots.addAll(gridY.getAllBullets());
                        if (gridY.HP < 0) {
                            toRemove.add(gridY);
                        }
                    }
                }
            }
        }
        for(EnemyShip ship : toRemove){
            switch (ship.eType){
                case YELLOW: ships.get(0)[ship.gridx][ship.gridy] = null; break;
                case RED: ships.get(1)[ship.gridx][ship.gridy] = null; break;
                case BLUE: ships.get(2)[ship.gridx][ship.gridy] = null; break;
                case COMMANDER: ships.get(3)[ship.gridx][ship.gridy] = null; break;
            }
        }
        return enemyShots;
    }

    public void drawAll(Paint p, Canvas c){
        for(EnemyShip[][] shipType : ships){
            for(EnemyShip[] gridX : shipType){
                for(EnemyShip gridY : gridX){
                    if(gridY != null) gridY.drawRect(p, c);
                }
            }
        }
    }
}
