package com.vip.marrakech.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class  ResetPasswordActivity extends AppCompatActivity implements OnCallBackListener {

    private EditText edt_password,edt_confirm_password;
    private Button btn_reset;
    private Communication communication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        communication = new Communication(this,this);
        initUI();


        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validator.isEmpty(edt_password)) {
                    Validator.setError(edt_password, "Enter Password");
                }else if (Validator.isEmpty(edt_confirm_password)) {
                    Validator.setError(edt_confirm_password, "Enter Confirm Password");
                }else if (!Validator.getText(edt_confirm_password).equals(Validator.getText(edt_password))) {
                    Validator.setError(edt_confirm_password, "Confirm Password doesn't match");
                }else {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("action","password/reset");
                    param.put("email", SessionManager.getEmail());
                    param.put("password",Validator.getText(edt_password));
                    param.put("c_password",Validator.getText(edt_confirm_password));
                    communication.callPOST(param);
                }
            }
        });
    }

    private void initUI() {
        Toolbar toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoTo.startWithFinish(ResetPasswordActivity.this,EmailVerificationActivity.class);
            }
        });
        edt_password = findViewById(R.id.edt_password);
        edt_confirm_password = findViewById(R.id.edt_confirm_password);
        btn_reset = findViewById(R.id.btn_reset);
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
            if (tag.equals("password/reset")){
                JSONObject data = null;
                try {
                    data = jsonObject.getJSONObject("data");
                    SessionManager.setName(data.getString("name"));
                    SessionManager.setEmail(data.getString("email"));
                    SessionManager.setRole(data.getString("role"));
                    SessionManager.setID(data.getString("id"));
                    GoTo.startWithFinish(ResetPasswordActivity.this,ResetSuccessActivity.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }
}
