package com.shahrir.nytimes_articles_search;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;


// webview of clicked articles with fragment

public class nytimes_saved_articles_webview extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nytimes_saved_web);

        WebView view = (WebView) findViewById(R.id.wvArticlesaved);
        view.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        view.loadUrl(nytimes_search_fragment.urlTextToLoad);
    }














}
