package com.vip.marrakech.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vip.marrakech.R;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.helpers.Validator;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener, OnCallBackListener {

    private EditText edt_email;
    private Button btn_send;
    private TextView btn_login;
    private Communication communication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        communication = new Communication(this,this);
        initUI();

        btn_send.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    private void initUI() {
        Toolbar toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              GoTo.startWithFinish(ForgotPasswordActivity.this,LoginActivity.class);
            }
        });
        edt_email = findViewById(R.id.edt_email);
        btn_send = findViewById(R.id.btn_send);
        btn_login = findViewById(R.id.btn_login);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_send){
            if (Validator.isEmpty(edt_email)){
                Validator.setError(edt_email,"Enter email");
            }else if (Validator.isNotEmail(edt_email)){
                Validator.setError(edt_email,"Enter valid email");
            }else {
                HashMap<String, String> param = new HashMap<>();
                param.put("action","password/sendVerificationCode");
                param.put("email",Validator.getText(edt_email));
                communication.callPOST(param);
            }
        }else {
            GoTo.startWithFinish(ForgotPasswordActivity.this,LoginActivity.class);
        }
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
            if (tag.equals("password/sendVerificationCode")){
                try {
                    SessionManager.setEmail(jsonObject.getString("email"));
                    Toast.makeText(this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    GoTo.startWithFinish(ForgotPasswordActivity.this,EmailVerificationActivity.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }
}
