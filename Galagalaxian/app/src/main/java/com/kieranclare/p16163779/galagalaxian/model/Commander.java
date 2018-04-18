package com.kieranclare.p16163779.galagalaxian.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by p16163779 on 13/02/2018.
 */

public class Commander extends EnemyShip implements Visitable {
    int score = 50;
    private Sprite spriteIdle1;
    private Sprite currentIdle;

    private Sprite currentSprite;

    private CommanderBeam commanderBeam;

    private float beamTime = 0;
    private boolean comX = false, comY = false, attackComplete = false, comAttack = false;
    private ArrayList<CapturedShip> capturedShips;
    private ArrayList<Bullet> capturedShipShots;
    private Effect effect;
    SoundEffects soundEffects;
    /**
     * @param widthIn
     * @param heightIn
     * @param xIn
     * @param yIn
     * @param speed
     * @param sprites
     */
    public Commander(float widthIn, float heightIn, float xIn, float yIn, float speed, Map<String, Bitmap> sprites, SoundEffects soundEffectsObject) {
        super(widthIn, heightIn, xIn, yIn, Color.CYAN, speed);
        MaxHP = 2;
        HP = MaxHP;
        eType = enemyType.COMMANDER;

        capturedShips = new ArrayList<>();
        capturedShipShots = new ArrayList<>();

        this.spriteIdle1 = new Sprite(sprites.get("commander1"), getWidth(), getHeight(), x, y);
        this.spriteIdle1.addFrameFromMaster(0, 0, (int) (spriteIdle1.masterBitmap.getWidth() * 0.5f), spriteIdle1.masterBitmap.getHeight());
        Sprite spriteIdle2 = new Sprite(sprites.get("commander2"), getWidth(), getHeight(), x, y);
        spriteIdle2.addFrameFromMaster(0, 0, (int) (spriteIdle2.masterBitmap.getWidth() * 0.5f), spriteIdle2.masterBitmap.getHeight());
        currentIdle = spriteIdle2;

        effect = new Effect(getWidth(), getHeight(), x, y, sprites, true);
        effect.setSoundEffectObject(soundEffectsObject);

        currentSprite = currentIdle;

        soundEffects = soundEffectsObject;
        bulletBank.setSprite(sprites.get("rocket"));
        commanderBeam = new CommanderBeam(getWidth(), getHeight(), x, y, sprites.get("beam"));
    }

    @Override
    public void fire(){
        if(ready) {
            bulletBank.fireNew(x + (getWidth()*0.5f), y + getHeight(), 0, 25, damageMultiplier);
            ready = false;
            firing = true;
            soundEffects.play("laser_default", 0.3f);
        }
    }

    @Override
    public void update(ArrayList<Bullet> bullets, PlayerShip ship, long delta){
        super.update(bullets, ship, delta);
        if(HP == 1){currentIdle = spriteIdle1; currentSprite = currentIdle;}
        currentSprite.setPosition(x, y);
        if(currentSprite.animate){
            currentSprite.update();
        }
        if(comX && comY) {
            commanderBeam.update();
        }
        updateCapturedShips(bullets, ship, delta);
        effect.setPositon(x, y);
    }

    private void updateCapturedShips(ArrayList<Bullet> bullets, PlayerShip ship, long delta){
        ArrayList<CapturedShip> toRemove = new ArrayList<>();
        for(CapturedShip capturedShip : capturedShips){
            if(HP <= 0){
                capturedShip.setMasterShip(ship, false);
                ship.addCapturedShip(capturedShip);
                capturedShips.remove(capturedShip);
                toRemove.add(capturedShip);
            }
            capturedShip.updateAsEnemy(x, y - (capturedShip.getHeight() * 1.2f), bullets, ship);
            //if(firing){capturedShip.fire();}
            capturedShipShots.addAll(capturedShip.getAllBullets());
        }
        capturedShips.removeAll(toRemove);
    }

    @Override
    public ArrayList<Bullet> getAllBullets(){
        ArrayList<Bullet> temp = bulletBank.getAllBullets();
        temp.addAll(capturedShipShots);
        capturedShipShots.clear();
        return temp;
    }

    @Override
    public void drawRect(Paint p, Canvas c){
        super.drawRect(p, c);
        if(HP > 0 || !currentSprite.end) {
            currentSprite.draw(c, p, rotation);
        }

        if(comX && comY) {
            commanderBeam.draw(p, c);
        }
        for(CapturedShip ship : capturedShips){
            ship.drawRect(p, c);
        }
    }

    public void setUpAttack(){
        attackComplete = false;
        gridMode = false;
        comAttack = true;
    }

    public void commanderAttack(ArrayList<Bullet> playerShots, PlayerShip playerShip, long delta,
                                 Point screenSize, GridPattern gridPattern, Map<String, Bitmap> spritesheet){
        if(HP <= 0){
            comX = false;
            comY = false;
            attackComplete = true;
            comAttack = false;
            gridPattern.effects.add(effect);
            updateCapturedShips(playerShots, playerShip, delta);
            soundEffects.stop("tractor_beam1");
            return;
        }
        BulletCollisionCheck(playerShots);
        ShipCollisionCheck(playerShip);
        currentIdle.setPosition(x, y);
        updateCapturedShips(playerShots, playerShip, delta);
        boolean gridYPos = false, gridXPos = false;
        if(!comX || !comY && HP > 0) {
            if(!attackComplete) {
                if (x < screenSize.x * 0.5f) {
                    x += 10;
                } else {
                    x = screenSize.x * 0.5f;
                    comX = true;
                }
                if (y < screenSize.y * 0.7f) {
                    y += 10;
                } else {
                    y = screenSize.y * 0.7f;
                    comY = true;
                }
            }else{
                float[] gridPos = gridPattern.getPosition(gridx, gridy, 3);
                if(x > gridPos[0]) {
                    x -= 10;
                }else{
                    x = gridPos[0];
                    gridXPos = true;
                }
                if(y > gridPos[1]) {
                    y -= 10;
                }else{
                    y = gridPos[1];
                    gridYPos = true;
                }
            }
        }

        if(gridYPos && gridXPos){
            comAttack = false;
            comX = false;
            comY = false;
            attackComplete = true;
            comAttack = false;
            gridMode = true;
            //gridPattern.ships.get(3)[gridx][gridy] = this;
            return;
        }

        if(comX && comY){
            float beamTickSize = 0.1f;
            beamTime += delta * beamTickSize;
            float beamDelay = 20000;
            if (beamTime < beamDelay) {
                soundEffects.playLooped("tractor_beam1");
                commanderBeam.setPosition(x + (commanderBeam.getWidth()*0.5f), y + getWidth(), screenSize);
                //commanderBeam.setPosition(screenSize);
                if(playerShip.rectCollision(commanderBeam.commanderBeam)){
                    playerShip.HP = 0;
                    CapturedShip temp = new CapturedShip(playerShip.getWidth(),
                            playerShip.getHeight(), playerShip.x, playerShip.y,
                            playerShip.speed, spritesheet, soundEffects);
                    temp.HP = temp.MaxHP;
                    temp.setMasterShip(this, true);
                    capturedShips.add(temp);
                    soundEffects.stop("tractor_beam1");
                    beamTime = beamDelay;
                }
            }else{
                comX = false;
                comY = false;
                attackComplete = true;
                soundEffects.stop("tractor_beam1");
            }

        }

    }

    public Effect getEffect(){
        return effect;
    }

    public Commander getShip(Visitor visitor) {
        return visitor.getShip(this);
    }

    public EnemyShip getEnemyShip(Visitor visitor) {
        return visitor.getShip(this);
    }

    @Override
    public void setReady(Visitor visitor, boolean r) {
        visitor.setReady(this, r);
    }

    @Override
    public void generateAttackPattern(Visitor visitor, Point screenSize, GridPattern gridPattern, PointF t) {
    }

    @Override
    public void executeAttackPattern(Visitor visitor, ArrayList<Bullet> playerShots, PlayerShip playerShip, long delta, Point screenSize, GridPattern gridPattern, Map<String, Bitmap> spritesheet) {
        visitor.executeAttackPattern(this, playerShots, playerShip, delta, screenSize, gridPattern, spritesheet);
    }

    @Override
    public Effect getExplosion(Visitor visitor) {
        return visitor.getExplosion(this);
    }

    @Override
    public int getScore(Visitor visitor) {
        return visitor.getScore(this);
    }

    @Override
    public YellowBug getYellow(Visitor visitor) {
        return null;
    }

    @Override
    public RedBug getRed(Visitor visitor) {
        return null;
    }

    @Override
    public BlueBug getBlue(Visitor visitor) {
        return null;
    }

    @Override
    public Commander getCommander(Visitor visitor) {
        return visitor.getShip(this);
    }

    public void startFlying(Visitor visitor) {
        visitor.startFlying(this);
    }

    public float getDelay(Visitor visitor) {
        return visitor.getDelay(this);
    }

    public boolean isGridMode(Visitor visitor) {
        return visitor.isGridMode(this);
    }

    @Override
    public void setGridMode(Visitor visitor, boolean gm) {
        visitor.setGridMode(this, gm);
    }

    @Override
    public boolean isFinished(Visitor visitor) {
        return visitor.isFinished(this);
    }

    @Override
    public void update(Visitor visitor, ArrayList<Bullet> bullets, PlayerShip ship, long delta) {
        visitor.update(this, bullets, ship, delta);
    }

    @Override
    public ArrayList<Bullet> getBullets(Visitor visitor) {
        return visitor.getBullets(this);
    }

    @Override
    public int getHP(Visitor visitor) {
        return visitor.getHP(this);
    }

    @Override
    public int getShotChance(Visitor visitor) {
        return visitor.getShotChance(this);
    }

    @Override
    public PointF getPos(Visitor visitor) {
        return visitor.getPos(this);
    }

    @Override
    public void setPos(Visitor visitor, PointF p) {
        visitor.setPos(this, p);
    }

    @Override
    public int[] getGridPos(Visitor visitor) {
        return visitor.getGridPos(this);
    }

    public void setGridPos(Visitor visitor, Point p) {
        visitor.setGridPos(this, p);
    }

    @Override
    public void fire(Visitor visitor) {
        visitor.fire(this);
    }

    @Override
    public void addPath(Visitor visitor, AttackPattern p) {
        visitor.addPath(this, p);
    }

    @Override
    public enemyType getEnemyType(Visitor visitor) {
        return visitor.getEnemyType(this);
    }

    @Override
    public void drawRect(Visitor visitor, Paint p, Canvas c) {
        visitor.drawRect(this, p, c);
    }

}
