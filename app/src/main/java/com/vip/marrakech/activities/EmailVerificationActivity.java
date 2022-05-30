package com.vip.marrakech.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.vip.marrakech.R;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.helpers.Validator;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EmailVerificationActivity extends AppCompatActivity implements OnCallBackListener {

    private TextView tv_email, tv_resend;
    private EditText edt_code;
    private Button btn_verify;
    private Communication communication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);
        communication = new Communication(this,this);

        initUI();

        tv_email.setText(getResources().getString(R.string.you_have_received_a_verification_code_on_xxxxxx_gmail_com,SessionManager.getEmail()));


        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validator.isEmpty(edt_code)){
                    Validator.setError(edt_code,"Enter Verification code");
                }else {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("action","password/verifyCode");
                    param.put("verification_code",Validator.getText(edt_code));
                    communication.callPOST(param);
                }
            }
        });

        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, String> param = new HashMap<>();
                param.put("action","password/sendVerificationCode");
                param.put("email",SessionManager.getEmail());
                communication.callPOST(param);

            }
        });
    }

    private void initUI() {
        Toolbar toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoTo.startWithFinish(EmailVerificationActivity.this,ForgotPasswordActivity.class);
            }
        });
        tv_email = findViewById(R.id.tv_email);
        edt_code = findViewById(R.id.edt_code);
        btn_verify = findViewById(R.id.btn_verify);
        tv_resend = findViewById(R.id.tv_resend);
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
            if (tag.equals("password/verifyCode")){
                try {
                    JSONObject data = jsonObject.getJSONObject("data");
                    SessionManager.setToken(data.getString("token"));
                    GoTo.startWithFinish(EmailVerificationActivity.this,ResetPasswordActivity.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (tag.equals("password/sendVerificationCode")){
                try {
                    SessionManager.setEmail(jsonObject.getString("email"));
                    Toast.makeText(this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }
}
