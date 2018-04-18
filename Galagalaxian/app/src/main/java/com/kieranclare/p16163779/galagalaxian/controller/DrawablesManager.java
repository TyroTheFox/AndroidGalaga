package com.kieranclare.p16163779.galagalaxian.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.kieranclare.p16163779.galagalaxian.R;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by p16163779 on 26/02/2018.
 */

public class DrawablesManager {

    private Context context;
    private Map<String, Bitmap> bitmaps;
    private static Map<String, Integer> drawableIDs;

    public DrawablesManager(Context context){
        this.context = context;
        bitmaps = new HashMap<>();
        drawableIDs = new HashMap<>();
        loadDrawables(R.drawable.class);
        setUpBitmaps();
    }

    private static void loadDrawables(Class<?> clz){
        final Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            try {
                drawableIDs.put(field.getName(), field.getInt(clz));
            } catch (Exception e) {
                continue;
            }
        }
    }

    private void setUpBitmaps(){
        for(String key : drawableIDs.keySet()){
            bitmaps.put(key, BitmapFactory.decodeResource(context.getResources(), drawableIDs.get(key)));
        }
    }

    public Bitmap getBitmap(String s){
        return bitmaps.get(s);
    }

}
