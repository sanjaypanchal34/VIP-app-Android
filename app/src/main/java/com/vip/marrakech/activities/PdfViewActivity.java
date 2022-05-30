package com.vip.marrakech.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.vip.marrakech.R;
import com.vip.marrakech.helpers.GoTo;

public class PdfViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        Bundle bundle = GoTo.getIntent(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(bundle.getString("title"));
        }
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setBuiltInZoomControls(true);


        if (bundle.getString("url").endsWith(".pdf")) {
                    webView.loadUrl(bundle.getString("url"));
        }else {
            String html = "<html><body><img src=\"" + bundle.getString("url") + "\" width=\"100%\" height=\"100%\"\"/></body></html>";
            webView.loadData(html, "text/html", null);
        }
        //webView.loadUrl( bundle.getString("url"));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
