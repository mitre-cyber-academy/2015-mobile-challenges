package com.mitre.vtomic.ctfhard;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import android.os.Environment;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.*;
import android.net.Uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;


public class PlaylistAddActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        WebView web_view = (WebView) findViewById(R.id.webview);
        web_view.loadUrl("http://10.0.2.2:4567/playlist-add");
        web_view.addJavascriptInterface(new WebAppInterface(this), "Android");
        web_view.setWebViewClient(new WebViewClient());

        WebSettings webSettings = web_view.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }
}