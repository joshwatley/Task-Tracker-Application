package com.example.goaltrackerapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class UserGuideActivity extends AppCompatActivity {
    WebView webview_ug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guide);

        webview_ug = findViewById(R.id.webview_ug);
        webview_ug.setWebViewClient(new WebViewClient());
        webview_ug.loadUrl("file:///android_asset/ugIndex.html");


    }
}
