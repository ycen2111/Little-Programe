package com.edinburgh.ewireless.activity;

/**
 * Author: yijianzheng
 * Date: 15/04/2023 18:13
 * <p>
 * Notes:
 */
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.edinburgh.ewireless.R;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient()); // Use a WebViewClient to handle navigation
        webView.getSettings().setJavaScriptEnabled(true); // Enable JavaScript if needed
        webView.getSettings().setLoadWithOverviewMode(true); // Enable viewport handling
        webView.getSettings().setUseWideViewPort(true); // Make WebView use wide viewport
        webView.setInitialScale(1); // Set initial zoom level

        String httpString = getIntent().getStringExtra("httpString");

        webView.loadData(httpString, "text/html; charset=UTF-8", null);
    }

    // Handle the back button press for WebView navigation
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}