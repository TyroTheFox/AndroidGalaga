package com.kieranclare.p16163779.galagalaxian.controller;

import android.content.Context;

import com.kieranclare.p16163779.galagalaxian.model.Score;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Project: Galagalaxian
 * Created by p16163779 on 07/03/2018.
 */

public class Scoreboard {

    private TreeSet<Score> scores;
    private int maxScoresNo = 10;

    private JSONDataHandler scoreboardDataHandler;

    public Scoreboard(Context context) {
        scoreboardDataHandler = new JSONDataHandler(context);
        scores = new TreeSet<Score>();
        if(scoreboardDataHandler.isFileExist("scoreboard.txt")) {
            JSONArray jsonScores = new JSONArray();
            try {
                 jsonScores = scoreboardDataHandler.readFromFile("scoreboard.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
            for(int i = 0; i < jsonScores.length(); i++){
                try {
                    scores.add(new Score(jsonScores.getJSONObject(i).getString("name"),
                            jsonScores.getJSONObject(i).getInt("score")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(scores.size() == 0){
                defaultScores();
            }
        } else {
            defaultScores();
        }
    }

    private void defaultScores(){
        scores.add(new Score("Jim of the Seven Isles", 100));
        scores.add(new Score("Phili Dolphinia", 1200));
        scores.add(new Score("An Eggplant in Glasses", 1500));
        scores.add(new Score("Your Dark Reflection Except in a Clown Wig", 1700));
        scores.add(new Score("John Johnsonson", 2000));
        scores.add(new Score("Lilly Pod", 2500));
        scores.add(new Score("Jeff, Licker of Toads", 3000));
        scores.add(new Score("Farfig, Chief Back-Scratcher to the King", 4000));
    }

    public boolean checkScore(int score){
        Iterator<Score> it = scores.iterator();
        boolean newHighScore = false;
        while(it.hasNext()){
            if(score >= it.next().getScore()){
                newHighScore = true;
            }
        }
        return newHighScore;
    }

    public void addNewHighScore(String name, int score){
        scores.add(new Score(name, score));
        if(scores.size() > maxScoresNo){
            scores.remove(scores.last());
        }
        writeToStorage();
    }

    public void writeToStorage(){
        JSONArray array = new JSONArray();
        for (Score score : scores) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", score.getName());
                jsonObject.put("score", score.getScore());
                array.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        scoreboardDataHandler.writeToFile(array, "scoreboard.txt");
    }

    public ArrayList<Score> returnScoresAsArrayList(){
        Iterator<Score> it = scores.iterator();
        ArrayList<Score> returnedScores = new ArrayList<>();
        while(it.hasNext()){
            returnedScores.add(it.next());
        }
        return returnedScores;
    }

    public String[] returnScoresAsStringArray(){
        ArrayList<Score> temp = returnScoresAsArrayList();
        String[] returnedScores = new String[temp.size()];
        int i = 0;
        for(Score score : temp){
            returnedScores[i] = "" + (i+1) + ": " + score.getName() + " - " + score.getScore();
            i++;
        }
        return returnedScores;
    }

}
