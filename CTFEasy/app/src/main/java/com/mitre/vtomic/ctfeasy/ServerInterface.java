package com.mitre.vtomic.ctfeasy;



import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException;

import java.net.URL;

import java.net.URLConnection;

import java.net.URLEncoder;


public class ServerInterface {
    public static final String SERVER_URL = "http://10.0.2.2:4567/";

    public static String sendMsg(String message){
        String data = message;
        return executeHttpRequest(data);
    }

    private static String executeHttpRequest(String data) {
        String result = "";
        try {
            URL url = new URL(SERVER_URL+data);
            URLConnection connection = url.openConnection();

            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            DataInputStream dataIn = new DataInputStream(connection.getInputStream());
            String inputLine;
            while ((inputLine = dataIn.readLine()) != null) {
                result += inputLine;
            }
            dataIn.close();
        } catch (IOException e) {
        /*
         * In case of an error, we're going to return a null String. This
         * can be changed to a specific error message format if the client
         * wants to do some error handling. For our simple app, we're just
         * going to use the null to communicate a general error in
         * retrieving the data.
         */
            e.printStackTrace();
            result = null;
        }

        return result;
    }

}
