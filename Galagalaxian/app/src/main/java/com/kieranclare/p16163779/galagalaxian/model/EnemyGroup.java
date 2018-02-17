package com.kieranclare.p16163779.galagalaxian.model;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by p16163779 on 09/02/2018.
 */

public abstract class EnemyGroup {
    LinkedList<EnemyShip> ships;
    ArrayList<EnemyShip> activeShips;
    public float startDelay;
    public boolean ready = false, flying = false;
    public float delay = 4, tickSize = 0.1f, time = 0;

    public boolean alpha = false, beta = false, gamma = false, delta = false;

    enum enemyType {YELLOW, RED, BLUE, COMMANDER};

    public EnemyGroup(){
        ships = new LinkedList<>();
        startDelay = 4;
    }

    public EnemyGroup(float delay){
        ships = new LinkedList<>();
        startDelay = delay;
    }

    public void addShip(EnemyShip ship){
        ships.add(ship);
    }

    public void addShips(ArrayList<EnemyShip> groupShips){
        ships.addAll(groupShips);
    }

    public ArrayList<Bullet> updateAll(ArrayList<Bullet> playerShots, PlayerShip playerShip, long delta){
        ArrayList<Bullet> enemyShots = new ArrayList<>();

        if(ready) {
            time += delta * tickSize;
            if (time >= delay) {
                if (ships.size() > 0) {
                    EnemyShip temp = ships.pop();
                    temp.startFlying();
                    activeShips.add(temp);
                } else {
                    ready = false;
                }
                time = 0;
            }
        }

        if(ships.size() > 0) {
            ArrayList<EnemyShip> toRemove = new ArrayList<>();
            for (EnemyShip ship : ships) {
                ship.update(playerShots, playerShip, delta);
                enemyShots.addAll(ship.getAllBullets());
                if (ship.HP <= 0) {
                    toRemove.add(ship);
                }
            }
            activeShips.removeAll(toRemove);
        }

        if(activeShips.size() > 0) {
            ArrayList<EnemyShip> toRemove = new ArrayList<>();
            for (EnemyShip ship : activeShips) {
                ship.update(playerShots, playerShip, delta);
                enemyShots.addAll(ship.getAllBullets());
                if (ship.HP <= 0) {
                    toRemove.add(ship);
                }
                if (!ship.flying) {
                    toRemove.add(ship);
                }
            }
            activeShips.removeAll(toRemove);
        }else{
            flying = false;
        }

        return enemyShots;
    }

    public void clearFlightPaths(){
        for(EnemyShip ship : ships){
            ship.clearPaths();
        }
    }

    public void generateAPAlpha(float endX, float endY, float delay){
        for(EnemyShip ship : ships){
            AttackPattern temp = new AttackPattern(delay);
            temp.attackName = 0;
            temp.moveTo(ship.x, ship.y);
            temp.rLineTo(700, 1000);
            temp.rLineTo(100, 1000);
            temp.lineTo(endX, endY);
            temp.setLastPoint(endX, endY);
            //temp.close();
            ship.addPath(temp);
        }
        alpha = true;
    }

    public void generateAPAlpha(GridPattern gridPattern, float delay){
        for(EnemyShip ship : ships){
            gridPattern.addShip(ship);
            AttackPattern temp = new AttackPattern(delay);
            temp.attackName = 0;
            temp.moveTo(ship.x, ship.y);
            temp.rLineTo(700, 1000);
            temp.rLineTo(100, 1000);
            int tempInt = 0;
            switch (ship.eType){
                case YELLOW: tempInt = 0; break;
                case RED: tempInt = 1; break;
                case BLUE: tempInt = 2; break;
                case COMMANDER: tempInt = 3; break;
            }
            temp.lineTo(gridPattern.getPosition(ship.gridx, ship.gridy, tempInt)[0],
                    gridPattern.getPosition(ship.gridx, ship.gridy, tempInt)[1]);
            //temp.setLastPoint(gridPattern.getPosition(ship.gridx, ship.gridy, tempInt)[0],
                  //  gridPattern.getPosition(ship.gridx, ship.gridy, tempInt)[1]);
            //temp.close();
            ship.addPath(temp);
        }
        alpha = true;
    }

    public void generateNewAPAlpha(GridPattern gridPattern, float delay){
        for(EnemyShip ship : ships){
            AttackPattern temp = new AttackPattern(delay);
            temp.attackName = 0;
            temp.moveTo(ship.x, ship.y);
            temp.rLineTo(700, 1000);
            temp.rLineTo(100, 1000);
            int tempInt = 0;
            switch (ship.eType){
                case YELLOW: tempInt = 0; break;
                case RED: tempInt = 1; break;
                case BLUE: tempInt = 2; break;
                case COMMANDER: tempInt = 3; break;
            }
            temp.lineTo(gridPattern.getPosition(ship.gridx, ship.gridy, tempInt)[0],
                    gridPattern.getPosition(ship.gridx, ship.gridy, tempInt)[1]);
            //temp.setLastPoint(gridPattern.getPosition(ship.gridx, ship.gridy, tempInt)[0],
                   // gridPattern.getPosition(ship.gridx, ship.gridy, tempInt)[1]);
            //temp.close();
            ship.addPath(temp);
        }
        alpha = true;
    }
}
