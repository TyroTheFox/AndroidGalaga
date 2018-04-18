package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by p16163779 on 10/02/2018.
 */

public class GridPattern{

    public float x = 0, y = 0, width = 0, height = 0;

    private Visitable positionsYellow[][], positionsRed[][], positionsBlue[][], positionsCom[][];
    public List<Visitable[][]> ships = new ArrayList<>();
    public List<Effect> effects = new ArrayList<>();

    private int positionYellow = 0, currentRowYellow = 0, currentColumnYellow = 0;
    private int positionRed = 0, currentRowRed = 0, currentColumnRed = 0;
    private int positionBlue = 0, currentRowBlue = 0, currentColumnBlue = 0;
    private int positionCom = 0, currentRowCom = 0, currentColumnCom = 0;

    private int columns, rows, arrayLength;
    public int shipCount = 0;
    int score = 0;

    CenteredGrid grid;

    private Point screenSize;
    public ArrayList<Visitable> deadShips = new ArrayList<>();
    ShipVisitor shipVisitor = new ShipVisitor();

    /**
     *
     * @param columns
     * @param rows
     */
    public GridPattern(int columns, int rows, Point screenS){
        width = screenS.x ;
        height = screenS.y*0.5f;

        positionsYellow = new Visitable[columns][rows];
        positionsRed = new Visitable[columns][rows];
        positionsBlue = new Visitable[columns][rows];
        positionsCom = new Visitable[columns][rows];
        ships.add(positionsYellow);
        ships.add(positionsRed);
        ships.add(positionsBlue);
        ships.add(positionsCom);
        arrayLength = columns * rows;
        this.columns = columns;
        this.rows = rows;
        screenSize = screenS;

    }

    public void initialiseGrid(float xPos, float shipWidth, float shipHeight){
        int tempWidth = currentColumnYellow;
        tempWidth = Math.max(tempWidth, currentColumnRed);
        tempWidth = Math.max(tempWidth, currentColumnBlue);
        tempWidth = Math.max(tempWidth, currentColumnCom);
        int fullWidth = tempWidth + 1;
        int fullHeight = (currentRowYellow + currentRowRed + currentRowBlue + currentRowCom) + 4;
        grid = new CenteredGrid(xPos, 0, shipWidth, shipHeight, fullWidth, fullHeight);
        grid.addYellowHeight(currentRowYellow);
        grid.addRedHeight(currentRowRed);
        grid.addBlueHeight(currentRowBlue);
        grid.addCommanderHeight(currentRowCom);
    }

    public void addShip(Visitable enemyShip) {
        if(enemyShip.getEnemyType(shipVisitor) == EnemyShip.enemyType.YELLOW){
            addYellowBug(enemyShip);
        }
        if(enemyShip.getEnemyType(shipVisitor) == EnemyShip.enemyType.RED){
            addRedBug(enemyShip);
        }
        if(enemyShip.getEnemyType(shipVisitor) == EnemyShip.enemyType.BLUE){
            addBlueBug(enemyShip);
        }
        if(enemyShip.getEnemyType(shipVisitor) == EnemyShip.enemyType.COMMANDER){
            addCommanderBug(enemyShip);
        }
    }

    public void addShips(ArrayList<Visitable> groupShips) {
        for(Visitable enemyShip : groupShips){
            if(enemyShip.getEnemyType(shipVisitor) == EnemyShip.enemyType.YELLOW){
                addYellowBug(enemyShip);
            }
            if(enemyShip.getEnemyType(shipVisitor) == EnemyShip.enemyType.RED){
                addRedBug(enemyShip);
            }
            if(enemyShip.getEnemyType(shipVisitor) == EnemyShip.enemyType.BLUE){
                addBlueBug(enemyShip);
            }
            if(enemyShip.getEnemyType(shipVisitor) == EnemyShip.enemyType.COMMANDER){
                addCommanderBug(enemyShip);
            }
        }
    }

    private void addYellowBug(Visitable enemyShip) {
        if(positionYellow >= arrayLength){
            return;
        }
        if(currentRowYellow >= rows){
            return;
        }
        if(currentColumnYellow >= columns){
            currentRowYellow++;
            currentColumnYellow = 0;
        }
        ships.get(0)[currentColumnYellow][currentRowYellow] = enemyShip;
        enemyShip.setGridPos(shipVisitor, new Point(currentColumnYellow, currentRowYellow));
        currentColumnYellow++;
        positionYellow++;
        shipCount++;
    }

    private void addRedBug(Visitable enemyShip) {
        if(positionRed >= arrayLength){
            return;
        }
        if(currentRowRed >= rows){
            return;
        }
        if(currentColumnRed >= columns){
            currentRowRed++;
            currentColumnRed = 0;
        }
        ships.get(1)[currentColumnRed][currentRowRed] = enemyShip;
        enemyShip.setGridPos(shipVisitor, new Point(currentColumnRed, currentRowRed));
        currentColumnRed++;
        positionRed++;
        shipCount++;
    }

    private void addBlueBug(Visitable enemyShip) {
        if(positionBlue >= arrayLength){
            return;
        }
        if(currentRowBlue >= rows){
            return;
        }
        if(currentColumnBlue >= columns){
            currentRowBlue++;
            currentColumnBlue = 0;
        }
        ships.get(2)[currentColumnBlue][currentRowBlue] = enemyShip;
        enemyShip.setGridPos(shipVisitor, new Point(currentColumnBlue, currentRowBlue));
        currentColumnBlue++;
        positionBlue++;
        shipCount++;
    }

    private void addCommanderBug(Visitable enemyShip) {
        if(positionCom >= arrayLength){
            return;
        }
        if(currentRowCom >= rows){
            return;
        }
        if(currentColumnCom >= columns){
            currentRowCom++;
            currentColumnCom = 0;
        }
        ships.get(3)[currentColumnCom][currentRowCom] = enemyShip;
        enemyShip.setGridPos(shipVisitor, new Point(currentColumnCom, currentRowCom));
        currentColumnCom++;
        positionCom++;
        shipCount++;
    }

    public boolean isEmpty(){
        return ships.size() <= 0;
    }

    Visitable getRandomShip(){
        Random random = new Random();
        ArrayList<Visitable> notFlying = new ArrayList<>();
        for(Visitable[][] shipType : ships){
            for(Visitable[] gridX : shipType){
                for(Visitable gridY : gridX){
                    if(gridY != null){
                        if(gridY.isGridMode(shipVisitor)) {
                            notFlying.add(gridY);
                        }
                    }
                }
            }
        }
        if(notFlying.size() == 0){
            return null;
        }
        Visitable temp = notFlying.get(random.nextInt(notFlying.size()));
        temp.startFlying(shipVisitor);
        return temp;
    }

    Visitable getRandomShip(EnemyShip.enemyType enemyType){
        Random random = new Random();
        ArrayList<Visitable> notFlying = new ArrayList<>();
        for(Visitable[][] shipType : ships){
            for(Visitable[] gridX : shipType){
                for(Visitable gridY : gridX){
                    if(gridY != null){
                        if(gridY.isGridMode(shipVisitor) && gridY.getEnemyType(shipVisitor).equals(enemyType)) {
                            notFlying.add(gridY);
                        }
                    }
                }
            }
        }
        if(notFlying.size() == 0){
            return null;
        }
        Visitable temp = notFlying.get(random.nextInt(notFlying.size()));
        temp.startFlying(shipVisitor);
        return temp;
    }

    float[] getPosition(int gridX, int gridY, int enemyType) {
        return grid.getPosition(gridX, gridY, enemyType);
    }

    public ArrayList<Bullet> updateAll(ArrayList<Bullet> playerShots, PlayerShip playerShip, long delta){
        ArrayList<Bullet> enemyShots = new ArrayList<>();
        ArrayList<Effect> toRemove = new ArrayList<>();
        int t = 0;
        for(Visitable[][] shipType : ships){
            for(Visitable[] gridX : shipType){
                for(Visitable gridY : gridX){
                    if(gridY != null) {
                        gridY.update(shipVisitor, playerShots, playerShip, delta);
                        enemyShots.addAll(gridY.getBullets(shipVisitor));
                        if (gridY.getHP(shipVisitor) <= 0) {
                            score += gridY.getScore(shipVisitor);
                            effects.add(gridY.getExplosion(shipVisitor));
                            Visitable deadShip = gridY;
                            if(!deadShips.contains(deadShip)){
                                deadShips.add(deadShip);
                            }
                            gridY = null;
                        }
                    }
                    if(gridY != null){
                        if(gridY.isGridMode(shipVisitor)) {
                            gridY.setPos(shipVisitor, new PointF(getPosition(gridY.getGridPos(shipVisitor)[0], gridY.getGridPos(shipVisitor)[1], t)[0],
                                    getPosition(gridY.getGridPos(shipVisitor)[0], gridY.getGridPos(shipVisitor)[1], t)[1]));
                            //gridY.update(shipVisitor, playerShots, playerShip, delta);
                            //enemyShots.addAll(gridY.getBullets(shipVisitor));
                        }
                    }
                }
            }
            t++;
        }

        for(Effect effect : effects){
            effect.update();
            if(effect.spriteExplosion.end){
                toRemove.add(effect);
            }
        }
        effects.removeAll(toRemove);
        grid.update(screenSize);
        return enemyShots;
    }

    public int getScore(){
        int temp = score;
        score = 0;
        return temp;
    }

    public void drawAll(Paint p, Canvas c){
        for(Visitable[][] shipType : ships){
            for(Visitable[] gridX : shipType){
                for(Visitable gridY : gridX){
                    if(gridY != null && gridY.getHP(shipVisitor) > 0) gridY.drawRect(shipVisitor, p , c);
                }
            }
        }
        for(Effect effect : effects){
            effect.draw(p, c);
        }
    }
}
