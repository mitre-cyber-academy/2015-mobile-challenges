package com.mitre.ctfhard;

import android.app.Activity;
import android.os.Bundle;

import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.*;


public class ErrorActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String URL = ServerInterface.getInstance().getServerUrl();

        WebView web_view = (WebView) findViewById(R.id.webview);
        web_view.loadUrl(URL + "search-users");
        web_view.addJavascriptInterface(new WebAppInterface(this), "Android");
        web_view.setWebViewClient(new WebViewClient());

        WebSettings webSettings = web_view.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }
}
