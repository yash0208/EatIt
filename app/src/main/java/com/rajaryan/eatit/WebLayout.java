package com.rajaryan.eatit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebLayout extends AppCompatActivity {
    WebView webView;
    String link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_layout);
        webView = (WebView) findViewById(R.id.web);
        Intent i=getIntent();
        link=i.getStringExtra("link");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        Toast.makeText(getApplicationContext(),link,Toast.LENGTH_LONG).show();
        webView.loadUrl(link);
    }

    public void back(View view) {
        onBackPressed();
    }
}