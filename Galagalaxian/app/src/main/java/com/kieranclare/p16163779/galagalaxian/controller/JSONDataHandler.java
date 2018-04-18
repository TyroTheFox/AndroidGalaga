package com.kieranclare.p16163779.galagalaxian.controller;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Project: Galagalaxian
 * Created by p16163779 on 07/03/2018.
 */

public class JSONDataHandler {
    Context context;

    public JSONDataHandler(Context context) {
        this.context = context;
    }

    public boolean isFileExist(String file) {
        File findfile = context.getFileStreamPath(file);
        return findfile.exists();
    }

    public boolean deleteFile(String file){
        File findfile = context.getFileStreamPath(file);
        if(findfile.exists()){
            findfile.delete();
        }
        return !findfile.exists();
    }

    public void writeToFile(JSONArray array, String file) {
        String sBody = array.toString();
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(file, Context.MODE_PRIVATE));
            outputStreamWriter.write(array.toString());
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public JSONArray readFromFile(String file) throws IOException {
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(file);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        JSONArray j = new JSONArray();

        try {
            j = new JSONArray(ret);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return j;
    }
}
