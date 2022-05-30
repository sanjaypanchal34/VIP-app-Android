package com.vip.marrakech.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.vip.marrakech.R;
import com.vip.marrakech.admin.activities.AdminMainActivity;
import com.vip.marrakech.fcm.MyFirebaseMessagingService;
import com.vip.marrakech.user.activities.UserMainActivity;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.helpers.MyHandler;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.user.dialogs.BookingSummaryDialog;
import com.vip.marrakech.vendor.activities.VendorMainActivity;

import java.util.Locale;


public class SplashActivity extends AppCompatActivity implements MyHandler.OnHandlerListener {
    private MyHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(!SessionManager.isLogged()){
            SessionManager.clean();
        }
        handler = new MyHandler(this);
        SessionManager.setVendorConfirmPassword(false);
    }

    @Override
    public void onRun() {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.setLocale(new Locale(SessionManager.getLanguage().toLowerCase()));
        resources.updateConfiguration(config, dm);
        if (SessionManager.isLogged()) {
            if (SessionManager.getRole().equals("Admin")){
                GoTo.startWithFinish(SplashActivity.this, AdminMainActivity.class);
            }else if (SessionManager.getRole().equals("Vendor")){
                GoTo.startWithFinish(SplashActivity.this, VendorMainActivity.class);
            }else {
                GoTo.startWithFinish(SplashActivity.this, UserMainActivity.class);
            }
        } else {
            GoTo.startWithFinish(SplashActivity.this, LoginActivity.class);
        }


    }


    @Override
    protected void onResume() {
        handler.setDelay(5000);
        super.onResume();
    }

    @Override
    protected void onPause() {
        handler.revoke();
        super.onPause();
    }
}
