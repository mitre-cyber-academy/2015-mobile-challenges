package com.mitre.playlistshare;

import android.content.Intent;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;


public class WebAppInterface {
    Context mContext;

    WebAppInterface(Context c){
        mContext = c;
    }

    @JavascriptInterface
    public void showLog(String text){
        Log.i("CtFHardJS", text);
    }

    @JavascriptInterface
    public String deviceBackup(String data){
        File sdCard = Environment.getExternalStorageDirectory();
        File file = new File(sdCard.getAbsolutePath() + "/PlaylistBackUp.txt");
        try {
            OutputStreamWriter osw = new OutputStreamWriter(mContext.openFileOutput("PlaylistBackUp.txt", mContext.MODE_APPEND | mContext.MODE_WORLD_READABLE));
            osw.write(data + ", ");
            osw.close();
        } catch (FileNotFoundException e) {
            Log.e("CtFHard", "file not found");
        }catch(java.io.IOException e){
            Log.e("CtFHard", "file not found");
        }
        return data;
    }

    @JavascriptInterface
    public void switchActivity(String activity){
        Intent startNew = new Intent(mContext, ErrorActivity.class);
        if(activity.equals("playlist")){
            startNew = new Intent(mContext, PlaylistActivity.class);
       }
        if(activity.equals("add-playlist")){
            startNew = new Intent(mContext, PlaylistAddActivity.class);
        }
        if(activity.equals("remove-playlist")){
            startNew = new Intent(mContext, PlaylistRemoveActivity.class);
        }
        if(activity.equals("search-users")){
            startNew = new Intent(mContext, SearchOtherActivity.class);
        }
        if(activity.equals("other-users")){
            startNew = new Intent(mContext, OtherUsersActivity.class);
        }
        if(activity.equals("other-playlist")){
            startNew = new Intent(mContext, OtherPlaylistActivity.class);
        }
        if(activity.equals("logout")){
            startNew = new Intent(mContext, MainActivity.class);
        }
        mContext.startActivity(startNew);
    }
}
