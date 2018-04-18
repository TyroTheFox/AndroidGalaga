package com.kieranclare.p16163779.galagalaxian.model;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by p16163779 on 01/03/2018.
 */

public class SoundEffects {
    SoundPool soundPool;
    private static Map<String, Integer> rawIDs;
    private static Map<String, Integer> soundIDs;
    private static Map<String, Integer> streamIDs;
    public SoundEffects(){
        rawIDs = new HashMap<>();
        soundIDs = new HashMap<>();
        streamIDs = new HashMap<>();
        AudioAttributes audioAttrib = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder().setAudioAttributes(audioAttrib).setMaxStreams(6).build();
    }

    public void addIDs(HashMap<String, Integer> ids, Context context){
        rawIDs.putAll(ids);
        loadSounds(context);
    }

    public void loadSounds(Context context){
        for(String id : rawIDs.keySet()){
            soundIDs.put(id, soundPool.load(context, rawIDs.get(id), 1));
        }
    }

    public void play(String name){
        soundPool.play(soundIDs.get(name), 1, 1, 1, 0, 1.0f);
    }

    public void play(String name, float volume){
        soundPool.play(soundIDs.get(name), volume, volume, 1, 0, 1.0f);
    }

    public void playOnce(String name){
        if(!streamIDs.containsKey(name)) {
            streamIDs.put(name, soundPool.play(soundIDs.get(name), 1, 1, 1, 0, 1.0f));
        }
    }

    public void playLooped(String name){
        if(!streamIDs.containsKey(name)) {
            streamIDs.put(name, soundPool.play(soundIDs.get(name), 1, 1, 1, -1, 1.0f));
        }
    }

    public void playLooped(String name, float volume){
        if(!streamIDs.containsKey(name)) {
            streamIDs.put(name, soundPool.play(soundIDs.get(name), volume, volume, 1, -1, 1.0f));
        }
    }

    public void stop(String name){
        if(streamIDs.containsKey(name)) {
            soundPool.stop(streamIDs.get(name));
            streamIDs.remove(name);
        }
    }
    /**
     * Releases Sound Pool Instance
     */
    public void release(){
        soundPool.release();
    }
}
