package com.vip.marrakech.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
import java.util.Locale;

public class EmailVerificationActivity2 extends AppCompatActivity implements OnCallBackListener {

    private TextView tv_email, tv_resend;
    private EditText edt_code;
    private Button btn_verify;
    private Communication communication;
    private boolean isExpired = true;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification2);
        communication = new Communication(this,this);

        initUI();

        tv_email.setText(getResources().getString(R.string.you_have_received_a_verification_code_on_xxxxxx_gmail_com,SessionManager.getMobile()));


       startTimer();

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(isExpired){
                   new AlertDialog.Builder(EmailVerificationActivity2.this)
                           .setMessage("OTP Expired.")
                           .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                               }
                           }).show();

               }else{
                   if (Validator.isEmpty(edt_code)){
                       Validator.setError(edt_code,"Enter Verification code");
                   }else {
                       HashMap<String, String> param = new HashMap<>();
                       param.put("action","verify/user");
                       param.put("verification_code",Validator.getText(edt_code));
                       param.put("email", SessionManager.getEmail());
                       communication.callPOST(param);
                   }
               }
            }
        });

        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isExpired) {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("action", "resend/verify/code");
                    param.put("contact_number", SessionManager.getMobile().replaceAll(" ",""));
                    communication.callPOST(param);
                }

            }
        });
    }

    private void startTimer() {
        new CountDownTimer(120000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                isExpired = false;
                tv_resend.setText(String.format(Locale.ENGLISH,"%d %s",120-counter,"Seconds"));
                counter++;
            }
            @Override
            public void onFinish() {
                isExpired = true;
                tv_resend.setText(getResources().getString(R.string.resend));
            }
        }.start();
    }

    private void initUI() {
        Toolbar toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoTo.startWithFinish(EmailVerificationActivity2.this,RegisterActivity.class);
            }
        });
        tv_email = findViewById(R.id.tv_email);
        edt_code = findViewById(R.id.edt_code);
        btn_verify = findViewById(R.id.btn_verify);
        tv_resend = findViewById(R.id.tv_resend);
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
            if (tag.equals("verify/user")){
                try {
                    Toast.makeText(EmailVerificationActivity2.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    GoTo.startWithFinish(EmailVerificationActivity2.this,LoginActivity.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (tag.equals("resend/verify/code")){
                try {
                    counter = 0;
                    startTimer();
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
