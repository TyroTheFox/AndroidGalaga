package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

/**
 * Created by p16163779 on 06/02/2018.
 */

public class Level
{
    private LinkedList<AttackGroup> groups;
    private ArrayList<AttackGroup> activeGroups;
    private ArrayList<Bullet> enemyShots;

    private boolean ready = false;
    private float delay = 0;
    private float time = 0;
    private float beamTime = 0;
    private GridPattern gridPattern;
    public boolean gridMode = false;
    private Point screenSize;
    private Commander attackingCommander;
    ShipVisitor shipVisitor = new ShipVisitor();
    private boolean comAttack = false;
    private boolean enemyReady = false;
    private boolean started = false;
    private int deadCount = 0;
    private Random random = new Random();
    private Map<String, Bitmap> spriteSheet;
    SoundEffects soundEffects;
    private int noEntranceGroups = 4;

    public int bgm = 0;

    int score = 0;

    public Level(int w, int h, Point screenS, int yellow, int red, int blue, int com, Map<String, Bitmap> spritesheet, SoundEffects soundEffectsObject){
        spriteSheet = spritesheet;
        soundEffects = soundEffectsObject;
        ArrayList<EnemyShip> ships = new ArrayList<>();
        groups = new LinkedList<>();
        activeGroups = new ArrayList<>();
        ShipFactory shipFactory = new ShipFactory(screenS, spritesheet, soundEffects);
        screenSize = screenS;

        ArrayList<PointF> spawnPoints = new ArrayList<>();
        spawnPoints.add(new PointF(screenSize.x*0.3f, -screenSize.y*0.1f));
        spawnPoints.add(new PointF(screenSize.x*0.7f, -screenSize.y*0.1f));
        spawnPoints.add(new PointF(-screenSize.x*0.1f, screenSize.y*0.7f));
        spawnPoints.add(new PointF(screenSize.x + screenSize.x*0.1f, screenSize.y*0.7f));

        //Entrance
        gridPattern = new GridPattern(w, h, screenS);
        shipFactory.addSpawnPoints(spawnPoints);
        shipFactory.generateShips(yellow, red, blue, com);
        shipFactory.assignShipsToGrid(gridPattern);
        gridPattern.initialiseGrid(screenSize.x*0.5f, shipFactory.scaledWidth, shipFactory.scaledHeight);
        shipFactory.generateInitialGroups();
        groups = shipFactory.getGroups();

        boolean direction = false;
        for (AttackGroup group : groups) {
            if(group.getSpawnPoint().x >= screenSize.x*0.5f) {
               direction = true;
            }
            if(group.getSpawnPoint().y > screenSize.y*0.5f) {
                group.generateNewAPBeta(gridPattern, 0, direction);
            }else{
                group.generateNewAPAlpha(gridPattern, 0, direction);
            }
        }

    }

    public void updateAllGroups(ArrayList<Bullet> playerShots, PlayerShip playerShip, long delta){
        enemyShots = new ArrayList<>();
        if(ready){
            delay = 0;
            if(groups.size() > 0) {
                started = true;
                AttackGroup temp = groups.pop();
                temp.ready = true;
                activeGroups.add(temp);
                ready = false;
                if(groups.size() > 0) {
                    delay = groups.getFirst().startDelay;
                }
            }
        }
        float tickSize = 0.1f;
        time += tickSize;
        if (time >= delay) {
            ready = true;
            time = 0;
        }else{
            gridMode = true;
        }
        ArrayList<Effect> effects = new ArrayList<>();
        if(started) {
            ArrayList<AttackGroup> toRemove = new ArrayList<>();
            for (AttackGroup group : activeGroups) {
                enemyShots.addAll(group.updateAll(playerShots, playerShip, delta));
                if (group.finished) {
                    //score += group.getScore();
                    toRemove.add(group);
                }
            }
            activeGroups.removeAll(toRemove);
            if(activeGroups.size() == 0 && groups.size() == 0){
                enemyReady = true;
            }
        }
        enemyShots.addAll(gridPattern.updateAll(playerShots, playerShip, delta));
    }

    public void updateGrid(ArrayList<Bullet> playerShots, PlayerShip playerShip, long delta){
        if(comAttack){
            attackingCommander.commanderAttack(playerShots, playerShip, delta, screenSize, gridPattern, spriteSheet);
            if(attackingCommander.getHP(shipVisitor) <= 0){
                comAttack = false;
                score += attackingCommander.score;
                attackingCommander = null;
            }
        }
        if (enemyReady) {
            randomlySelectEnemyAttackPatterns(playerShip);
            int enemyRampUp = 0;
            if(groups.size() > Math.round(1.0f*(enemyRampUp *0.1f)) && groups.size() <= 3) {
                enemyReady = false;
               // enemyRampUp++;
            }
            /*for (AttackGroup group : groups) {
                group.generateNewAPYellow(gridPattern, 0, playerShip);
            }*/
            //groups.get(0).generateNewAPAlpha(gridPattern, 0);
        }
    }

    public ArrayList<Bullet> getEnemyShots(){
        return enemyShots;
    }

    public int getScore(){
        int temp = score;
        score = 0;
        return temp;
    }

    private void randomlySelectEnemyAttackPatterns(PlayerShip playerShip){
        int currentSize = groups.size();
        int rand = random.nextInt(10);
        if(random.nextBoolean()){
            Visitable temp = gridPattern.getRandomShip();
            if(temp == null){
                return;
            }
            EnemyShip tempShip = temp.getEnemyShip(shipVisitor);
            tempShip.ready = true;
            AttackGroup tempGroup = new AttackGroup(0, screenSize);
            tempGroup.setSoundEffects(soundEffects);
            tempGroup.addShip(temp);
            groups.add(tempGroup);
            boolean direction = false;
            if(tempShip.x >= screenSize.x*0.5f) {
                direction = true;
            }
            if(0 <= rand && rand < 5) {
                groups.getLast().generateNewAPAlpha(gridPattern, 0, direction);
            }
            if(5 <= rand && rand < 10) {
                groups.getLast().generateNewAPBeta(gridPattern, 0, direction);
            }
            if(tempShip.eType == EnemyShip.enemyType.COMMANDER){
                randomlySelectEnemyAttackPatterns(playerShip);
            }
        }else {
            if (0 <= rand && rand < 3) {
                Visitable temp = gridPattern.getRandomShip(EnemyShip.enemyType.YELLOW);
                if(temp == null){
                    return;
                }
                if(temp.getEnemyType(shipVisitor) == EnemyShip.enemyType.YELLOW) {
                    temp.setReady(shipVisitor, true);
                    temp.generateAttackPattern(shipVisitor, screenSize, gridPattern, new PointF(playerShip.x, playerShip.y));
                    AttackGroup tempGroup = new AttackGroup(0, screenSize);
                    tempGroup.setSoundEffects(soundEffects);
                    tempGroup.addShip(temp);
                    groups.add(tempGroup);
                    //groups.getLast().generateNewAPYellow(gridPattern, 0, playerShip);
                }
            }
            if (3 <= rand && rand < 6) {
                Visitable temp = gridPattern.getRandomShip(EnemyShip.enemyType.RED);
                if(temp == null){
                    return;
                }
                if(temp.getEnemyType(shipVisitor) == EnemyShip.enemyType.RED) {
                    temp.setReady(shipVisitor, true);
                    temp.generateAttackPattern(shipVisitor, screenSize, gridPattern, new PointF(playerShip.x, playerShip.y));
                    AttackGroup tempGroup = new AttackGroup(0, screenSize);
                    tempGroup.setSoundEffects(soundEffects);
                    tempGroup.addShip(temp);
                    groups.add(tempGroup);
                }
            }
            if (6 <= rand && rand < 7) {
                Visitable temp = gridPattern.getRandomShip(EnemyShip.enemyType.BLUE);
                if(temp == null){
                    return;
                }
                if(temp.getEnemyType(shipVisitor) == EnemyShip.enemyType.BLUE) {
                    temp.setReady(shipVisitor, true);
                    temp.generateAttackPattern(shipVisitor, screenSize, gridPattern, new PointF(playerShip.x, playerShip.y));
                    AttackGroup tempGroup = new AttackGroup(0, screenSize);
                    tempGroup.setSoundEffects(soundEffects);
                    tempGroup.addShip(temp);
                    groups.add(tempGroup);
                }
            }
            if (7 <= rand && rand < 10 && !comAttack) {
                Visitable temp = gridPattern.getRandomShip(EnemyShip.enemyType.COMMANDER);
                if(temp == null){
                    return;
                }
                if(temp.getEnemyType(shipVisitor) == EnemyShip.enemyType.COMMANDER) {
                    attackingCommander = temp.getCommander(shipVisitor);
                    attackingCommander.setUpAttack();
                    comAttack = true;
                    randomlySelectEnemyAttackPatterns(playerShip);
                }
            }
        }
    }

    public boolean shipsLeft(){
        if(gridPattern.deadShips.size() >= gridPattern.shipCount){
            deadCount += 0;
        }
        return gridPattern.deadShips.size() >= gridPattern.shipCount;
    }

    public ArrayList<EnemyShip> getActiveShips(){
        ArrayList<EnemyShip> temp = new ArrayList<>();
        for(Visitable[][] shipType : gridPattern.ships){
            for(Visitable[] gridX : shipType){
                for(Visitable gridY : gridX){
                    if(gridY != null) {
                        if(gridY.getHP(shipVisitor) > 0){
                            temp.add(gridY.getEnemyShip(shipVisitor));
                        }
                    }
                }
            }
        }

        if(gridPattern.deadShips.size()>0){
            for (Visitable enemyShip : gridPattern.deadShips){
                switch(enemyShip.getEnemyType(shipVisitor)) {
                    case BLUE:
                        BlueBug blueship = enemyShip.getBlue(shipVisitor);
                        if(blueship.dead){break;}
                        blueship.dead = true;
                        score += blueship.score;
                        temp.add(blueship);
                        break;
                    case RED:
                        RedBug redship = enemyShip.getRed(shipVisitor);
                        if(redship.dead){break;}
                        redship.dead = true;
                        score += redship.score;
                        temp.add(redship);
                        break;
                    case YELLOW:
                        YellowBug yship = enemyShip.getYellow(shipVisitor);
                        if(yship.dead){break;}
                        yship.dead = true;
                        score += yship.score;
                        temp.add(yship);
                        break;
                    case COMMANDER:
                        Commander ship = enemyShip.getCommander(shipVisitor);
                        if(ship.dead){break;}
                        ship.dead = true;
                        score += ship.score;
                        temp.add(ship);
                        break;
                }
            }
        }

        /*for (AttackGroup group : groups) {
            if(gridPattern.deadShips.size()>0){
                temp.addAll(group.deadShips);
                deadCount += group.deadShips.size();
                group.deadShips.clear();
            }
        }

        for (AttackGroup group : activeGroups) {
            if(gridPattern.deadShips.size()>0){
                temp.addAll(group.deadShips);
                deadCount += group.deadShips.size();
                group.deadShips.clear();
            }
        }*/

        /*ArrayList<EnemyShip> toRemove = new ArrayList<>();
        for (int i = 0; i < temp.size(); i++) {
            boolean doingNothing = true;
            if(temp.get(i).gridMode){
                doingNothing = false;
            }
            for (AttackGroup group : groups) {
                temp.addAll(group.deadShips);
                group.deadShips.clear();
                if (group.ships.contains(temp.get(i))) {
                    doingNothing = false;
                }
            }
            for (AttackGroup group : activeGroups) {
                temp.addAll(group.deadShips);
                group.deadShips.clear();
                if (group.ships.contains(temp.get(i))) {
                    doingNothing = false;
                }
            }
            if(doingNothing){
                toRemove.add(temp.get(i));
            }
        }
        temp.removeAll(toRemove);*/

        return temp;
    }

    public void drawAll(Paint p, Canvas c){
        for (AttackGroup group : groups) {
            group.drawAll(p, c);
        }
        for (AttackGroup group : activeGroups) {
            group.drawAll(p, c);
        }
        gridPattern.drawAll(p, c);
        if(comAttack) {
            attackingCommander.drawRect(p, c);
        }
    }
}
