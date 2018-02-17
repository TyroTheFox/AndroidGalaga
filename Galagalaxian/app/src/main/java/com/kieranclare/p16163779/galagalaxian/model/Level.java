package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by p16163779 on 06/02/2018.
 */

public class Level
{
    ArrayList<EnemyShip> ships;
    ArrayList<AttackGroup> groups;
    ArrayList<Bullet> enemyShots;
    private boolean flying = false, ready = false, waiting = false;
    private int listPos = 0;
    Paint paint = new Paint();
    int gridX = 5, gridY = 5, gridCount = 0, gridRow = 0;
    float blockWidth = 200, blockHeight = 200, margin = 5;
    public float delay = 4, tickSize = 0.1f, time = 0;
    private AttackPattern p, p2, p3, p4;
    private GridPattern gridPattern;
    private boolean gridMode = false;
    private Point screenSize;

    int enemyRampUp = 0;
    boolean enemyReady = true;

    private int noEntranceGroups = 4;

    public Level(){
        ships = new ArrayList<EnemyShip>();
        groups = new ArrayList<>();
    }

    public Level(int x, int y, int w, int h, int m, Point screenS){
        ships = new ArrayList<>();
        groups = new ArrayList<>();
        gridX = x;
        gridY = y;
        blockWidth = h;
        blockWidth = w;
        margin = m;
        screenSize = screenS;

       /* float startX = 100, startY = 100;
        p = new AttackPattern(0);
        p.moveTo(startX, startY);
        p.rLineTo(700, 1000);
        p.rLineTo(100, 1000);
        p.rLineTo(100, 100);
        p.close();

        p2 = new AttackPattern(0);
        p2.rMoveTo(100, 100);
        p2.rLineTo(700, 1000);
        p2.rLineTo(100, 1000);
        p2.rLineTo(700, 700);
        p2.close();

        p3 = new AttackPattern(3);
        p3.moveTo(700, 700);
        p3.rLineTo(700, 50);
        p3.rLineTo(100, 50);
        p3.rLineTo(startX, startY);
        p3.close();

        p4 = new AttackPattern(8);
        p4.moveTo(700, 700);
        p4.rLineTo(700, 50);
        p4.rLineTo(100, 50);
        p4.rLineTo(startX, startY);
        p4.close();*/

        //Entrance
        int entranceGroups = 0;
        int yellowNo = 19, yellowCount = 0;
        int redNo = 9, redCount = 0;
        int blueNo = 5, blueCount = 0;
        int comNo = 3, comCount = 0;
        ArrayList<EnemyShip> temp = new ArrayList<>();
        int shipsPerGroup = (yellowNo + redNo + blueNo + comNo)/noEntranceGroups;
        int shipCheck = 0, delay = 0;
        while((yellowNo + redNo + blueNo + comNo) > (yellowCount + redCount + blueCount + comCount)){
            if(shipCheck >= shipsPerGroup){
                createGroup(temp, 0);
                temp = new ArrayList<>();
                delay+=3;
                entranceGroups++;
                shipCheck = 0;
            }
            if(yellowCount < yellowNo) {
                EnemyShip tempShip = createYellowBug(50, 50, 400, 100);
                temp.add(tempShip);
                yellowCount++;
                shipCheck++;
            }
            if(redCount < redNo) {
                EnemyShip tempShip = createRedBug(50, 50, 400, 100);
                temp.add(tempShip);
                redCount++;
                shipCheck++;
            }
            if(blueCount < blueNo) {
                EnemyShip tempShip = createBlueBug(50, 50, 400, 100);
                temp.add(tempShip);
                blueCount++;
                shipCheck++;
            }
            if(comCount < comNo) {
                EnemyShip tempShip = createCommander(50, 50, 400, 100);
                temp.add(tempShip);
                comCount++;
                shipCheck++;
            }
        }

        gridPattern = new GridPattern(10, 5, 100, 100, screenS);
        gridPattern.yellowCount = yellowNo;
        gridPattern.redCount = redNo;
        gridPattern.blueCount = blueNo;
        gridPattern.comCount = comNo;

        for (EnemyGroup group : groups) {
            group.generateAPAlpha(gridPattern, 0);
        }
    }

    public void createGroup(ArrayList<EnemyShip> enemyShips, float delay){
        EnemyGroup temp = new AttackGroup(delay);
        temp.addShips(enemyShips);
        //temp.generateAPAlpha(1000, 1000, 0);
        groups.add(temp);
    }

    public EnemyShip createShip(float width, float height, float x, float y){
        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        EnemyShip temp = new EnemyShip(width, height, x, y, color, 20f);
        temp.setBonus(random.nextBoolean());
        if(gridCount >= gridX){
            gridCount = 0;
            gridRow++;
        }
        temp.gridx = gridCount;
        temp.gridy = gridRow;
        gridCount++;
        return temp;
    }

    public YellowBug createYellowBug(float width, float height, float x, float y){
        Random random = new Random();
        YellowBug temp = new YellowBug(width, height, x, y, 20f);
        temp.setBonus(random.nextBoolean());
        if(gridCount >= gridX){
            gridCount = 0;
            gridRow++;
        }
        temp.gridx = gridCount;
        temp.gridy = gridRow;
        gridCount++;
        return temp;
    }

    public RedBug createRedBug(float width, float height, float x, float y){
        Random random = new Random();
        RedBug temp = new RedBug(width, height, x, y, 20f);
        temp.setBonus(random.nextBoolean());
        if(gridCount >= gridX){
            gridCount = 0;
            gridRow++;
        }
        temp.gridx = gridCount;
        temp.gridy = gridRow;
        gridCount++;
        return temp;
    }

    public BlueBug createBlueBug(float width, float height, float x, float y){
        Random random = new Random();
        BlueBug temp = new BlueBug(width, height, x, y, 20f);
        temp.setBonus(random.nextBoolean());
        if(gridCount >= gridX){
            gridCount = 0;
            gridRow++;
        }
        temp.gridx = gridCount;
        temp.gridy = gridRow;
        gridCount++;
        return temp;
    }

    public Commander createCommander(float width, float height, float x, float y){
        Random random = new Random();
        Commander temp = new Commander(width, height, x, y, 20f);
        temp.setBonus(random.nextBoolean());
        if(gridCount >= gridX){
            gridCount = 0;
            gridRow++;
        }
        temp.gridx = gridCount;
        temp.gridy = gridRow;
        gridCount++;
        return temp;
    }

    public void GenerateShips(){
        Random random = new Random();
        for(int i = 0; i < gridX; i++) {
            for (int j = 0; j < gridY; j++){
                int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
                EnemyShip temp = new EnemyShip(blockWidth, blockHeight, blockWidth * i + (margin * i), blockHeight * j + (margin * j), color, 15f);
                temp.setBonus(random.nextBoolean());
                //temp.setNewPath(p);
                temp.addPath(p);
                temp.addPath(p2);
                temp.addPath(p3);
                ships.add(temp);
            }
        }
    }

    public void updateAll(ArrayList<Bullet> playerShots, PlayerShip playerShip, long delta){
        enemyShots = new ArrayList<>();
        if(ready){
            if(groups.size() - 1 >= listPos) {
                groups.get(listPos).startFlight();
                ready = false;
                listPos++;
            }
        }else{
            if(groups.size() - 1 >= listPos) {
                delay = groups.get(listPos).startDelay;
            }
        }
        if(gridMode){
            if(enemyReady) {
                EnemyShip temp = gridPattern.getRandomShip();
                ArrayList tempList = new ArrayList();
                tempList.add(temp);
                createGroup(tempList, 10);
                //if (groups.size() <= groups.size() + Math.round(enemyRampUp % 2)) {
                    enemyReady = false;
               // }
                enemyRampUp++;
                /*for (EnemyGroup group : groups) {
                    group.generateNewAPAlpha(gridPattern, 0);
                }*/
                groups.get(0).generateNewAPAlpha(gridPattern, 0);
            }
            enemyShots.addAll(gridPattern.updateAll(playerShots, playerShip, delta));
        }
        time += tickSize;
        if (time >= delay) {
            ready = true;
            time = 0;
        }
        ArrayList<EnemyGroup> toRemove = new ArrayList<>();
        boolean allGroupsAttacking = false;
        for (EnemyGroup group : groups) {
            enemyShots.addAll(group.updateAll(playerShots, playerShip, delta));
            if(group.ships.size() <= 0){
                /*for(EnemyShip ship : group.ships){
                    ship.flying = false;
                }*/
                toRemove.add(group);
            }
            if(!group.attack){
                toRemove.add(group);
            }
        }
        if(groups.size() <= 0){
            gridMode = true;
            enemyReady = true;
        }
        groups.removeAll(toRemove);
    }

    public ArrayList<Bullet> getEnemyShots(){
        return enemyShots;
    }

    public ArrayList<EnemyShip> getActiveShips(){
        ArrayList<EnemyShip> temp = new ArrayList<>();
        for (EnemyGroup group : groups) {
            if(group.flying) {
                for (EnemyShip ship: group.ships) {
                    if(ship.HP > 0){
                        temp.add(ship);
                    }
                }
            }
        }
        return temp;
    }

    public void drawAll(Paint p, Canvas c){
        /*for (EnemyShip ship : ships) {
            ship.drawRect(p, c);
        }*/
        for (EnemyGroup group : groups) {
            if(group.flying) {
                for (EnemyShip ship: group.ships) {
                    if(ship.HP > 0){
                        ship.drawRect(p, c);
                    }
                }
            }
        }
        gridPattern.drawAll(p, c);
    }

    public ArrayList<EnemyShip> getBlockList(){
        return ships;
    }
}
