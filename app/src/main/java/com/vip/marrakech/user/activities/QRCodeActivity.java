package com.vip.marrakech.user.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vip.marrakech.R;

import java.io.File;

public class QRCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        SimpleDraweeView ivCode = findViewById(R.id.iv_code);
        if (getIntent()!= null && getIntent().getStringExtra("code") != null) {
            ivCode.setImageURI(Uri.fromFile(new File(getIntent().getStringExtra("code"))));
        }
        Toolbar toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
