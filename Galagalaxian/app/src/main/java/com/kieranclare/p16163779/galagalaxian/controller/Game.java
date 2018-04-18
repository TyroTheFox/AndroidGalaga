package com.kieranclare.p16163779.galagalaxian.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;

import com.kieranclare.p16163779.galagalaxian.R;
import com.kieranclare.p16163779.galagalaxian.model.Background;
import com.kieranclare.p16163779.galagalaxian.model.Bullet;
import com.kieranclare.p16163779.galagalaxian.model.CapturedShip;
import com.kieranclare.p16163779.galagalaxian.model.EnemyShip;
import com.kieranclare.p16163779.galagalaxian.model.Level;
import com.kieranclare.p16163779.galagalaxian.model.PlayerShip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by p16163779 on 24/02/2018.
 */

public class Game {
    Paint paint = new Paint();
    private Level currentLevel;
    private LinkedList<Level> levels;
    private DrawablesManager drawablesManager;
    private SoundFXManager soundFXManager;
    private Background background;
    public MediaPlayer mediaPlayer;
    Context context;

    private PlayerShip playerShip;

    public int score = 0;

    private float inX, inY;

    Point screenSize; //Holds the screen size

    public enum GameState {WIN, LOSE, RUNNING, NEXTLEVEL, PAUSED};

    int shotsFired = 0, shotsHit = 0;
    public int accuracy = 0;

    public Game(Point screenS, Context context){
        screenSize = screenS;
        this.context = context;

        drawablesManager = new DrawablesManager(context);
        Map<String, Bitmap> bitmaps = new HashMap<>();
        bitmaps.put("yellow", drawablesManager.getBitmap("yellow"));
        bitmaps.put("red", drawablesManager.getBitmap("red"));
        bitmaps.put("blue", drawablesManager.getBitmap("blue"));
        bitmaps.put("commander", drawablesManager.getBitmap("commanderss"));
        bitmaps.put("commander1", drawablesManager.getBitmap("commanderss1"));
        bitmaps.put("commander2", drawablesManager.getBitmap("commanderss2"));
        bitmaps.put("rocket", drawablesManager.getBitmap("rocket"));
        bitmaps.put("explode", drawablesManager.getBitmap("enemyshipexplode"));
        bitmaps.put("beam", drawablesManager.getBitmap("commanderbeam"));
        bitmaps.put("captured", drawablesManager.getBitmap("capturedship"));
        bitmaps.put("player", drawablesManager.getBitmap("playership"));
        bitmaps.put("playerexplode", drawablesManager.getBitmap("playershipdeath"));

        soundFXManager = new SoundFXManager(context);

        levels = new LinkedList<>();
        //Level Grid Widths should always be odd or they're built lopsided and wont sit in the middle properly.
        int songSwingJedDing = R.raw.swingjeding;
        int songTheWill = R.raw.thewill;
        int songBeugel = R.raw.beugel;
        levels.add(new Level(11, 7, screenS, 9, 5, 5, 3, bitmaps, soundFXManager.getSoundEffectsObject()));
        levels.add(new Level(11, 7, screenS, 18, 18, 9, 1, bitmaps, soundFXManager.getSoundEffectsObject()));
        levels.add(new Level(11, 7, screenS, 22, 22, 22, 22, bitmaps, soundFXManager.getSoundEffectsObject()));
        levels.get(0).bgm = songSwingJedDing;
        levels.get(1).bgm = songTheWill;
        levels.get(2).bgm = songBeugel;
        currentLevel = levels.pop();

        mediaPlayer = MediaPlayer.create(context, currentLevel.bgm);
        mediaPlayer.setVolume(0.5f, 0.5f);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);

        float shipWidth = 200, shipHeight = 200, scale = 12, unitWidth = screenSize.x/shipWidth, unitHeight = screenSize.x/shipHeight,
                scaledWidth = unitWidth * scale, scaledHeight = unitHeight * scale;

        playerShip = new PlayerShip(scaledWidth, scaledHeight, (screenSize.x * 0.5f) - (scaledWidth * 0.5f), (screenSize.y*0.9f) - (scaledHeight * 0.5f),
                Color.BLUE, 0.1f, bitmaps, soundFXManager.getSoundEffectsObject());
        inX = playerShip.x;
        inY = playerShip.y;

        background = new Background(screenSize.x, screenSize.y, 0, 0);
    }

    public GameState update(long delta){
        //Set Initial GameState
        GameState gameState = GameState.RUNNING;
        //Get Shots Player fired
        ArrayList<Bullet> playerShots = playerShip.getAllBullets();

        //Collision against Captured Ships Controlled by Enemy Ships
        for(CapturedShip ship : playerShip.getCapturedShips()){
            if(!ship.slaveToEnemy) playerShots.addAll(ship.getAllBullets());
        }

        //Initialise EnemyShots ArrayList
        ArrayList<Bullet> enemyShots = new ArrayList<>();
        //Update look for all AttackGroups and Grid Position for current Level
        currentLevel.updateAllGroups(playerShots,playerShip,delta);
        //If all initial Attack Groups are finished
        if(currentLevel.gridMode) {
            //Update Grid Postions
            currentLevel.updateGrid(playerShots, playerShip, delta);
        }
        //Get all Enemy Shots
        enemyShots.addAll(currentLevel.getEnemyShots());

        //If the player is dead, set them to the bottom of the screen
        if(playerShip.HP <= 0){
            inX = (screenSize.x * 0.5f) - (playerShip.getWidth() * 0.5f);
            inY = (screenSize.y - 100) - (playerShip.getHeight() * 0.5f);
        }
        //Get all active ships
        ArrayList<EnemyShip> enemyShips = currentLevel.getActiveShips();
        //If no ships left to fight
        if(currentLevel.shipsLeft()){
            //Advance level
            gameState = nextLevel();
        }
        //Update Player
        playerShip.update(inX, inY, enemyShots, enemyShips);
        //If player is dead
        if(playerShip.isDead()){
            //Return a lose game state, don't bother doing anything else
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            return GameState.LOSE;
        }
        //Update Starfield Background
        background.update();
        //Increase record of current score
        score += currentLevel.getScore();
        //Return Game State
        return gameState;
    }

    /**
     * Return Player Ship
     * @return
     */
    public PlayerShip getPlayerShip(){
        return playerShip;
    }

    public int getAccuracy(){
        float calulatedaccuracy = 0;
        if(playerShip.fireCount > 0) {
            calulatedaccuracy = (((float)playerShip.shotsHit / (float)playerShip.fireCount) * 100f);
            playerShip.shotsHit = 0;
            playerShip.fireCount = 0;
        }
        accuracy = Math.round(calulatedaccuracy);
        return accuracy;
    }

    private GameState nextLevel(){
        mediaPlayer.stop();
        if(levels.size() > 0) {
            currentLevel = levels.pop();
            mediaPlayer = MediaPlayer.create(context, currentLevel.bgm);
            mediaPlayer.setVolume(0.5f, 0.5f);
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }else{
            mediaPlayer.release();
            mediaPlayer = null;
            return GameState.WIN;
        }
        return GameState.NEXTLEVEL;
    }

    public void draw(Canvas canvas){
        canvas.drawARGB(255, 0, 0, 0); //Add a background colour
        background.draw(paint, canvas);
        currentLevel.drawAll(paint, canvas);
        /*ball.draw(paint, canvas);*/
        playerShip.drawRect(paint, canvas);

        paint.setColor(Color.BLUE);
        canvas.drawRect(inX - 15, inY - 15, inX + 15, inY + 15, paint);
        paint.setColor(Color.RED);
        canvas.drawRect(inX, inY, inX + 5, inY + 5, paint);
        paint.reset();
    }

    public void pressUpdate(float xPos, float yPos, boolean shoot){
        inX = xPos;
        inY = yPos - (playerShip.getHeight() * 0.5f);
        if(shoot){
            playerShip.fire();
        }
    }


}
