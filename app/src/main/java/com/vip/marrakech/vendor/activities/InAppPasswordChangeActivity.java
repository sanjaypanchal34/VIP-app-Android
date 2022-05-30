package com.vip.marrakech.vendor.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vip.marrakech.R;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.helpers.Validator;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.vendor.dialog.InAppOTPVerifyDialog;
import com.vip.marrakech.vendor.dialog.SetProfilePasswordDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class InAppPasswordChangeActivity extends AppCompatActivity implements OnCallBackListener, InAppOTPVerifyDialog.OnOTPListener {

    private Toolbar toolBar;
    private EditText edt_old_password,edt_new_password,edt_confirm_password;
    private Button btn_confirm,btn_reset;
    private Communication communication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app_password_change);
        communication = new Communication(this,this);
        toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        edt_old_password = findViewById(R.id.edt_old_password);
        edt_new_password = findViewById(R.id.edt_new_password);
        edt_confirm_password = findViewById(R.id.edt_confirm_password);
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_reset = findViewById(R.id.btn_reset);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_old_password.getText().toString().isEmpty()){
                    Validator.setError(edt_old_password,"Enter Old Password");
                }else if (edt_new_password.getText().toString().isEmpty()){
                    Validator.setError(edt_new_password,"Enter New Password");
                }else if (edt_confirm_password.getText().toString().isEmpty()){
                    Validator.setError(edt_confirm_password,"Enter Confirm Password");
                }else if (!edt_confirm_password.getText().toString().equals(edt_new_password.getText().toString())){
                    Validator.setError(edt_confirm_password,"Confirm Password doesn't match");
                }else {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("action", "vendor/change/password");
                    param.put("vendor_id", SessionManager.getEncryptedID());
                    param.put("old_password",edt_old_password.getText().toString());
                    param.put("new_password",edt_new_password.getText().toString());
                    param.put("confirm_password",edt_confirm_password.getText().toString());
                    communication.callPOST(param);
                }
            }
        });


        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> param = new HashMap<>();
                param.put("action", "vendor/reset/password");
                param.put("vendor_id", SessionManager.getEncryptedID());
                communication.callPOST(param);

            }
        });
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) throws JSONException {
        if (tag.equals("vendor/change/password")){
            Toast.makeText(InAppPasswordChangeActivity.this, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }else if (tag.equals("vendor/reset/password")){
            Toast.makeText(InAppPasswordChangeActivity.this, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            InAppOTPVerifyDialog inAppOTPVerifyDialog = new InAppOTPVerifyDialog();
            inAppOTPVerifyDialog.setListener(this);
            inAppOTPVerifyDialog.show(getSupportFragmentManager(),"rest");
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }

    @Override
    public void onOTPVerify() {
        SetProfilePasswordDialog profilePasswordDialog = new SetProfilePasswordDialog();
        profilePasswordDialog.setListener(new SetProfilePasswordDialog.OnProfilePasswordSetListener() {
            @Override
            public void onProfilePassword() {
                SessionManager.ProfilePasswordSet(1);
                onBackPressed();
            }
        });
        profilePasswordDialog.show(getSupportFragmentManager(),"set");
    }
}
