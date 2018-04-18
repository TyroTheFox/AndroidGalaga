package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by p16163779 on 09/02/2018.
 */

public abstract class EnemyGroup {
    LinkedList<Visitable> ships;
    LinkedList<Visitable> activeShips;
    public float startDelay;
    public boolean ready = false, flying = false, finished = false;
    public float delay = 0, tickSize = 0.1f, time = 0;
    private Point screenSize;
    private PointF spawnPoint;

    int score = 0;

    public ArrayList<Effect> effects = new ArrayList<>();

    public ArrayList<EnemyShip> deadShips = new ArrayList<>();

    ShipVisitor shipVisitor = new ShipVisitor();

    public boolean alpha = false, beta = false, gamma = false, delta = false, special = false;

    public EnemyGroup(Point screenS){
        screenSize = screenS;
        ships = new LinkedList<>();
        activeShips = new LinkedList<>();
        startDelay = 4;
    }

    public EnemyGroup(float delay, Point screenS){
        screenSize = screenS;
        ships = new LinkedList<>();
        activeShips = new LinkedList<>();
        startDelay = delay;
    }

    public void setSpawnPoint(PointF sP){ spawnPoint = sP; }
    public PointF getSpawnPoint(){ return spawnPoint; }

    public void addShip(Visitable ship){
        ships.add(ship);
    }

    public void addShips(ArrayList<Visitable> groupShips){
        ships.addAll(groupShips);
    }

    public ArrayList<Bullet> updateAll(ArrayList<Bullet> playerShots, PlayerShip playerShip, long delta){
        Random random = new Random();
        ArrayList<Bullet> enemyShots = new ArrayList<>();
        boolean didSomething = false;
        if(ready && ships.size() > 0) {
            didSomething = true;
            time += delta * tickSize;
            if (time >= delay) {
                if (ships.size() > 0) {
                    Visitable temp = ships.pop();
                    temp.startFlying(shipVisitor);
                    if(!activeShips.contains(temp)){
                        activeShips.add(temp);
                    }
                    if(ships.size() > 0){
                        delay = ships.getFirst().getDelay(shipVisitor);
                    }
                } else {
                    ready = false;
                }
                time = 0;
            }
        }
        if(ships.size() > 0) {
            ArrayList<Visitable> toRemove = new ArrayList<>();
            for (Visitable ship : ships) {
                if(!ship.isGridMode(shipVisitor)) {
                    //ship.update(shipVisitor, playerShots, playerShip, delta);
                    //enemyShots.addAll(ship.getBullets(shipVisitor));
                }
                if (ship.getHP(shipVisitor) <= 0) {
                    score += ship.getScore(shipVisitor);
                    toRemove.add(ship);
                }
            }
            ships.removeAll(toRemove);
        }

        if(activeShips.size() > 0) {
            flying = true;
            ArrayList<Visitable> toRemove = new ArrayList<>();
            for (Visitable ship : activeShips) {
                if(!ship.isGridMode(shipVisitor)) {
                    if(random.nextInt(10) < ship.getShotChance(shipVisitor)){
                        if(ship.getPos(shipVisitor).x >= playerShip.x - (playerShip.getWidth()*2) &&
                                ship.getPos(shipVisitor).x <= playerShip.x + (playerShip.getWidth()*2)) {
                            ship.fire(shipVisitor);
                        }
                    }
                    //ship.update(shipVisitor, playerShots, playerShip, delta);
                    //enemyShots.addAll(ship.getBullets(shipVisitor));
                    if (ship.getHP(shipVisitor) <= 0) {
                        score += ship.getScore(shipVisitor);
                        toRemove.add(ship);
                    }
                    if (ship.isFinished(shipVisitor)) {
                        ship.setGridMode(shipVisitor, true);
                        toRemove.add(ship);
                    }
                }
            }
            activeShips.removeAll(toRemove);
            if(activeShips.size() == 0 && ships.size() == 0) {
                flying = false;
                finished = true;
            }
        }

        return enemyShots;
    }

    public int getScore(){
        int temp = score;
        score = 0;
        return temp;
    }

    public ArrayList<Effect> getEffects(){
        return effects;
    }

    public void drawAll(Paint p, Canvas c){
        /*if(ships.size() > 0) {
            for (Visitable ship : ships) {
                if(!ship.isGridMode(shipVisitor)) {
                    ship.drawRect(shipVisitor, p, c);
                }
            }
        }
        if(activeShips.size() > 0) {
            for (Visitable ship : activeShips) {
                if(!ship.isGridMode(shipVisitor)) {
                    ship.drawRect(shipVisitor, p, c);
                }
            }
        }*/
        for(Effect effect : effects){
            effect.draw(p, c);
        }
    }

    public void setAttackPattern(AttackPattern at){
        for(Visitable ship : ships){
            ship.addPath(shipVisitor, at);
        }
        special = true;
    }

    public void generateNewAPAlpha(GridPattern gridPattern, float delay, boolean right){
        for(Visitable ship : ships){
            AttackPattern temp = new AttackPattern(delay);
            temp.attackName = 0;
            temp.moveTo(ship.getPos(shipVisitor).x, ship.getPos(shipVisitor).y);
            if(right) {
                temp.lineTo(screenSize.x * 0.9f, screenSize.y * 0.6f);
                temp.arcTo(screenSize.x * 0.5f, screenSize.y * 0.6f, screenSize.x * 0.9f, screenSize.y * 0.7f, 0, 180, false);
            }else{
                temp.lineTo(screenSize.x * 0.1f, screenSize.y * 0.6f);
                temp.arcTo(screenSize.x * 0.1f, screenSize.y * 0.6f, screenSize.x * 0.5f, screenSize.y * 0.7f, 180, -180, false);
            }
            int tempInt = 0;
            if(ship.getEnemyType(shipVisitor) != null) {
                switch (ship.getEnemyType(shipVisitor)) {
                    case YELLOW:
                        tempInt = 0;
                        break;
                    case RED:
                        tempInt = 1;
                        break;
                    case BLUE:
                        tempInt = 2;
                        break;
                    case COMMANDER:
                        tempInt = 3;
                        break;
                }
            }
            gridPattern.ships.get(tempInt)[ship.getGridPos(shipVisitor)[0]][ship.getGridPos(shipVisitor)[1]] = ship;
            temp.lineTo(gridPattern.getPosition(ship.getGridPos(shipVisitor)[0], ship.getGridPos(shipVisitor)[1], tempInt)[0],
                    gridPattern.getPosition(ship.getGridPos(shipVisitor)[0], ship.getGridPos(shipVisitor)[1], tempInt)[1]);
            //temp.setLastPoint(gridPattern.getPosition(ship.gridx, ship.gridy, tempInt)[0],
                   // gridPattern.getPosition(ship.gridx, ship.gridy, tempInt)[1]);
            //temp.close();
            ship.addPath(shipVisitor, temp);
        }
        alpha = true;
    }

    public void generateNewAPBeta(GridPattern gridPattern, float delay, boolean right){
        for(Visitable ship : ships){
            AttackPattern temp = new AttackPattern(delay);
            temp.attackName = 1;
            temp.moveTo(ship.getPos(shipVisitor).x, ship.getPos(shipVisitor).y);
            temp.lineTo(screenSize.x * 0.5f, screenSize.y * 0.7f);
            if(!right) {
                temp.arcTo(screenSize.x * 0.25f, screenSize.y * 0.3f, screenSize.x * 0.75f, screenSize.y * 0.7f, 90, 180, false);
                temp.arcTo(screenSize.x * 0.25f, screenSize.y * 0.3f, screenSize.x * 0.75f, screenSize.y * 0.7f, 270, 180, false);
            }else {
                temp.arcTo(screenSize.x * 0.25f, screenSize.y * 0.3f, screenSize.x * 0.75f, screenSize.y * 0.7f, 90, -180, false);
                temp.arcTo(screenSize.x * 0.25f, screenSize.y * 0.3f, screenSize.x * 0.75f, screenSize.y * 0.7f, 270, -180, false);
            }
            int tempInt = 0;
            if(ship.getEnemyType(shipVisitor) != null) {
                switch (ship.getEnemyType(shipVisitor)) {
                    case YELLOW:
                        tempInt = 0;
                        break;
                    case RED:
                        tempInt = 1;
                        break;
                    case BLUE:
                        tempInt = 2;
                        break;
                    case COMMANDER:
                        tempInt = 3;
                        break;
                }
            }
            gridPattern.ships.get(tempInt)[ship.getGridPos(shipVisitor)[0]][ship.getGridPos(shipVisitor)[1]] = ship;
            temp.lineTo(gridPattern.getPosition(ship.getGridPos(shipVisitor)[0], ship.getGridPos(shipVisitor)[1], tempInt)[0],
                    gridPattern.getPosition(ship.getGridPos(shipVisitor)[0], ship.getGridPos(shipVisitor)[1], tempInt)[1]);
            //temp.setLastPoint(gridPattern.getPosition(ship.gridx, ship.gridy, tempInt)[0],
            // gridPattern.getPosition(ship.gridx, ship.gridy, tempInt)[1]);
            //temp.close();
            ship.addPath(shipVisitor, temp);
        }
        alpha = true;
    }

    /*public void generateNewAPYellow(GridPattern gridPattern, float delay, PlayerShip target){
        PointF t = new PointF(target.x, target.y);
        for(Visitable ship : ships){

        }
        special = true;
    }

    public void generateNewAPRed(GridPattern gridPattern, float delay, PlayerShip target){
        PointF t = new PointF(target.x, target.y);
        for(EnemyShip ship : ships){

        }
        special = true;
    }

    public void generateNewAPBlue(GridPattern gridPattern, float delay, PlayerShip target){
        PointF t = new PointF(target.x, target.y);
        for(EnemyShip ship : ships){
        }
        special = true;
    }*/
}
