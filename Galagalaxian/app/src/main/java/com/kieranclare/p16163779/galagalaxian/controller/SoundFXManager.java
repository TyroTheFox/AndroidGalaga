package com.kieranclare.p16163779.galagalaxian.controller;

import android.content.Context;

import com.kieranclare.p16163779.galagalaxian.R;
import com.kieranclare.p16163779.galagalaxian.model.SoundEffects;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by p16163779 on 01/03/2018.
 */

public class SoundFXManager {

    private Context context;
    private SoundEffects soundEffects;
    private static HashMap<String, Integer> rawIDs;

    public SoundFXManager(Context context){
        this.context = context;
        soundEffects = new SoundEffects();
        rawIDs = new HashMap<>();
        loadSounds(R.raw.class);
        setUpSoundEffects(context);
    }

    private static void loadSounds(Class<?> clz){
        final Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            try {
                rawIDs.put(field.getName(), field.getInt(clz));
            } catch (Exception e) {
                continue;
            }
        }
    }

    private void setUpSoundEffects(Context context){
        soundEffects.addIDs(rawIDs, context);
    }

    public SoundEffects getSoundEffectsObject(){ return soundEffects; }
}

