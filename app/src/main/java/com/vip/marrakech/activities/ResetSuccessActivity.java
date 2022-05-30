package com.vip.marrakech.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vip.marrakech.R;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.helpers.SessionManager;

public class ResetSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_success);

        TextView tv_name = findViewById(R.id.tv_name);
        Button btn_login = findViewById(R.id.btn_login);
        tv_name.setText(SessionManager.getName());
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoTo.startWithFinish(ResetSuccessActivity.this,LoginActivity.class);
            }
        });
    }
}
