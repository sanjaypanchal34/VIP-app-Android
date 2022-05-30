package com.vip.marrakech.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.vip.marrakech.R;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.retrofit.ApiClient;

import org.apache.http.util.EncodingUtils;

import java.util.Locale;


public class PaymentActivity extends AppCompatActivity {

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);


        Toolbar toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        webView = findViewById(R.id.webView);
        String url = getIntent().getStringExtra("url");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        //webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setSupportZoom(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setClickable(true);

        webView.setWebChromeClient(new WebChromeClient() {

        });
        String postData = getIntent().getStringExtra("post_data")+"&lan="+SessionManager.getLanguage();
        Log.e("POST DATA::::", postData);

        webView.postUrl(url, EncodingUtils.getBytes(postData, "BASE64"));


    }


    public class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            // progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            // TODO Auto-generated method stub

            super.onPageFinished(view, url);
            // progressBar.setVisibility(View.GONE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            Log.e("REDIRECT::::", url);
            //view.loadUrl(url);
            if (url.contains("success_stripe")) {
                new AlertDialog.Builder(PaymentActivity.this)
                        .setMessage(getResources().getString(R.string.payment_s))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Resources resources = getResources();
                                DisplayMetrics dm = resources.getDisplayMetrics();
                                Configuration config = resources.getConfiguration();
                                config.setLocale(new Locale(SessionManager.getLanguage().toLowerCase()));
                                resources.updateConfiguration(config, dm);
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("status", 1);
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
                            }
                        })
                        .show();
            } else if (url.contains("error_stripe")) {
                new AlertDialog.Builder(PaymentActivity.this)
                        .setMessage(getResources().getString(R.string.payment_f))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .show();
            }
            return true;
        }

    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
