package com.example.p16163779.arkandriod;

import android.util.Log;

import java.util.Random;
import java.util.Vector;

/**
 * <h1>Test</h1>
 * A class to get back into practice with Java
 * @author Kieran Clare
 * @since 27/10/2017
 * @version 1
 */

public class Test {
    //Class Variables
    private String weaponType = "Board with a Nail";
    private float percentageCharge = 0;
    protected Vector<String> playerNames;
    private int weaponsHeld = 0;
    private int maxWeaponsHeld = 3;

    /**
     * <h2>Constructor</h2>
     * Initialises the Vector
     */
    public Test(){
        playerNames = new Vector<String>();
    }

    /**
     * <h2>Set Weapons Held</h2>
     * Sets a new value for weaponsHeld but only if it's less than or equal
     * maxWeaponsHeld
     * @param heldIn
     */
    public void setWeaponsHeld(int heldIn){
        if(heldIn <= maxWeaponsHeld){
            weaponsHeld = heldIn;
        }
    }

    /**
     * <h2>Get Weapons Held</h2>
     * Getter
     * @return weaponsHeld
     */
    public int getWeaponsHeld(){
        return weaponsHeld;
    }

    /**
     * <h2>Initialise Charge</h2>
     * Randomly selects a number between 1 and 50 then sets percentageCharge to that number
     */
    public void initialiseCharge(){
        Random rn = new Random();
        int range = 50 - 1 + 1;
        percentageCharge = rn.nextInt(range) + 1;
    }

    /**
     * <h2>Get Percentage Charge</h2>
     * Getter
     * @return percentageCharge
     */
    public float getPercentageCharge(){
        return percentageCharge;
    }

    /**
     * <h2>Add Player Name</h2>
     * Adds names to Player Name Vector
     * @param playerName
     */
    public void addPlayerName(String playerName){
        playerNames.add(playerName);
    }

    /**
     * <h2>Output Player Names</h2>
     * Writes the contents of the Player Names Vector to the Debug
     */
    public void outputPlayerNames(){
        for (String name : playerNames) {
            Log.d("Name", name);
        }
    }
}