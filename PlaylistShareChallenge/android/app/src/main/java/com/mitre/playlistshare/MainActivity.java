package com.mitre.playlistshare;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Environment;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        clearApplicationData();
        //Context mcontext = getApplicationContext();
        //deleteCache(mcontext);
        //deleteData(mcontext);

        ServerInterface.getInstance().init(getApplicationContext()); // only call once
        ServerInterface.getInstance().loadServerInfo(); // only call once
        String URL = ServerInterface.getInstance().getServerUrl();
        WebView web_view = (WebView) findViewById(R.id.webview);
        web_view.loadUrl(URL + "login");
        web_view.addJavascriptInterface(new WebAppInterface(this), "Android");
        web_view.setWebViewClient(new WebViewClient());

        WebSettings webSettings = web_view.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("CtFHard", "**************** File /data/data/com.mitre.vtomic.CtFHard/" + s + " DELETED *******************");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }
}
