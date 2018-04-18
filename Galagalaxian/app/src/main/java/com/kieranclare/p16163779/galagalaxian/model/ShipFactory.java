package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

/**
 * Created by p16163779 on 23/02/2018.
 */

public class ShipFactory {
    private Random random = new Random();
    private Point screenSize;

    public float scaledWidth, scaledHeight;

    private int yellowCount = 0;
    private int redCount = 0;
    private int blueCount = 0;
    private int comCount = 0;

    private LinkedList<AttackGroup> groups;
    private ArrayList<PointF> spawnPoints;
    private ArrayList<Visitable> ships;

    private int entranceGroups = 0;
    private float noEntranceGroups = 4;

    private int spawnPointNo = 0;
    private PointF spawnPoint;

    private Map<String, Bitmap> spriteSheet;
    SoundEffects soundEffects;
    ShipVisitor shipVisitor = new ShipVisitor();

    public ShipFactory(Point screenS, Map<String, Bitmap> spritesheet, SoundEffects soundEffectsObject){
        spriteSheet = spritesheet;
        screenSize = screenS;
        ships = new ArrayList<>();
        groups = new LinkedList<>();
        spawnPoints = new ArrayList<>();
        soundEffects = soundEffectsObject;
    }

    public void addSpawnPoints(ArrayList<PointF> sp){
        spawnPoints.addAll(sp);
        spawnPoint = spawnPoints.get(spawnPointNo);
    }

    public LinkedList<AttackGroup> getGroups(){
        return groups;
    }

    public void generateShips(int yellow, int red, int blue, int commander){
        float perShipDelay = 5;
        float shipWidth = 200, shipHeight = 200, scale = 12, unitWidth = screenSize.x/shipWidth, unitHeight = screenSize.x/shipHeight;
        scaledWidth = unitWidth * scale;
        scaledHeight = unitHeight * scale;

        while(yellow > yellowCount || red > redCount || blue > blueCount || commander > comCount){
            if(yellowCount < yellow) {
                YellowBug tempShip = createYellowBug(scaledWidth, scaledHeight, spawnPoint.x, spawnPoint.y, perShipDelay, spriteSheet, soundEffects);
                ships.add(tempShip);
                yellowCount++;
            }
            if(redCount < red) {
                RedBug tempShip = createRedBug(scaledWidth, scaledHeight, spawnPoint.x, spawnPoint.y, perShipDelay, spriteSheet, soundEffects);
                ships.add(tempShip);
                redCount++;
            }
            if(blueCount < blue) {
                BlueBug tempShip = createBlueBug(scaledWidth, scaledHeight, spawnPoint.x, spawnPoint.y, perShipDelay, spriteSheet, soundEffects);
                ships.add(tempShip);
                blueCount++;
            }
            if(comCount < commander) {
                Commander tempShip = createCommander(scaledWidth, scaledHeight, spawnPoint.x, spawnPoint.y, perShipDelay, spriteSheet, soundEffects);
                ships.add(tempShip);
                comCount++;
            }
        }
    }

    public void assignShipsToGrid(GridPattern gridPattern){
        gridPattern.addShips(ships);
    }

    public void generateInitialGroups(){
        int shipTotal = yellowCount + redCount + blueCount + comCount;
        int shipsPerGroup = Math.round(shipTotal/noEntranceGroups);
        int totalGroup = Math.round(shipTotal/shipsPerGroup);
       /* if(shipTotal % shipsPerGroup != 0){
            shipsPerGroup = Math.round(shipTotal/noEntranceGroups+1);
        }*/
        int shipCheck = 0, startDelay = 0;
        float perGroupDelay = 15;
        ArrayList<Visitable> temp = new ArrayList<>();
        Collections.shuffle(ships);
        int shipsLeft = ships.size();
        for(Visitable ship : ships) {
            ship.setPos(shipVisitor, spawnPoint);
            temp.add(ship);
            shipCheck++;
            shipsLeft--;
            if(shipCheck >= shipsPerGroup){
                createGroup(temp, perGroupDelay, spawnPoint);
                temp = new ArrayList<>();
                //startDelay = perGroupDelay;
                entranceGroups++;
                spawnPointNo++;
                if(spawnPointNo >= spawnPoints.size()){
                    spawnPointNo = 0;
                }
                spawnPoint = spawnPoints.get(spawnPointNo);
                shipCheck = 0;
            }
            if(entranceGroups == totalGroup && shipsLeft+1 > 0){
                int countEnd = shipTotal - (1 + shipsLeft);
                for(int i = shipTotal-1; i > countEnd; i--) {
                    if(!temp.contains(ships.get(i))) {
                        temp.add(ships.get(i));
                    }
                }
                createGroup(temp, perGroupDelay, spawnPoint);
                temp = new ArrayList<>();
                //startDelay = perGroupDelay;
                entranceGroups++;
                spawnPointNo++;
                if(spawnPointNo >= spawnPoints.size()){
                    spawnPointNo = 0;
                }
                spawnPoint = spawnPoints.get(spawnPointNo);
                shipCheck = 0;
            }
        }
    }

    public void createGroup(ArrayList<Visitable> enemyShips, float delay){
        AttackGroup temp = new AttackGroup(delay, screenSize);
        temp.addShips(enemyShips);
        groups.add(temp);
    }

    public void createGroup(ArrayList<Visitable> enemyShips, float delay, PointF sp){
        AttackGroup temp = new AttackGroup(delay, screenSize);
        temp.addShips(enemyShips);
        temp.setSpawnPoint(sp);
        groups.add(temp);
    }

    public YellowBug createYellowBug(float width, float height, float x, float y, float perShipDelay, Map<String, Bitmap> spriteSheet, SoundEffects soundEffectsObject){
        YellowBug temp = new YellowBug(width, height, x, y, 20f, spriteSheet, soundEffectsObject);
        temp.delay = perShipDelay;
        temp.shotChance = 1;
        return temp;
    }

    public RedBug createRedBug(float width, float height, float x, float y, float perShipDelay, Map<String, Bitmap> spriteSheet, SoundEffects soundEffectsObject){
        RedBug temp = new RedBug(width, height, x, y, 20f, spriteSheet, soundEffectsObject);
        temp.delay = perShipDelay;
        temp.shotChance = 2;
        return temp;
    }

    public BlueBug createBlueBug(float width, float height, float x, float y, float perShipDelay, Map<String, Bitmap> spriteSheet, SoundEffects soundEffectsObject){
        BlueBug temp = new BlueBug(width, height, x, y, 20f, spriteSheet, soundEffectsObject);
        temp.delay = perShipDelay;
        temp.shotChance = 7;
        return temp;
    }

    public Commander createCommander(float width, float height, float x, float y, float perShipDelay, Map<String, Bitmap> spriteSheet, SoundEffects soundEffectsObject){
        Commander temp = new Commander(width, height, x, y, 20f, spriteSheet, soundEffectsObject);
        temp.delay = perShipDelay;
        temp.shotChance = 1;
        return temp;
    }
}
