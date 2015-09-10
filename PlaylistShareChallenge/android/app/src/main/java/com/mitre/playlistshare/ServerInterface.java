package com.mitre.playlistshare;


import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerInterface {
    private static final String TAG = "SERVER_INTERFACE";
    private static String SERVER_URL = "";
    private static Context appContext = null;

    private static ServerInterface instance = new ServerInterface();

    public static ServerInterface getInstance() {
        if(instance == null) {
            instance = new ServerInterface();
        }
        return instance;
    }

    // Make sure you only call this once - from MyApplication.
    public void init(final Context context) {
        appContext = context.getApplicationContext();
    }

    private ServerInterface() {

    }

    public void loadServerInfo() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(appContext.getAssets().open("server.txt")));

            // do reading - should only need first line
            String ipPort  = reader.readLine();
            SERVER_URL = "http://"+ipPort+"/";
        } catch (IOException e) {
            //log the exception
            Log.e(TAG, "Error Loading server info...");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                    Log.e(TAG, "Error closing reader...");
                }
            }
        }
    }

    public String getServerUrl() {
        return SERVER_URL;
    }
}
