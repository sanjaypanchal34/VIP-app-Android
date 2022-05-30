package com.vip.marrakech.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vip.marrakech.R;
import com.vip.marrakech.admin.activities.AdminMainActivity;
import com.vip.marrakech.customs.calender.OnCalenderListener;
import com.vip.marrakech.dialogs.CalenderDialog;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.helpers.Validator;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.user.activities.UserMainActivity;
import com.vip.marrakech.user.models.MyDate;
import com.vip.marrakech.vendor.activities.VendorMainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, OnCallBackListener {

    private EditText edt_email, edt_password;
    private CheckBox cb_remember;
    private TextView tv_forgot_password, tv_register;
    private Button btn_login;
    private Communication communication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SessionManager.setLogged(false);
        SessionManager.clean();

        communication = new Communication(this, this);
        initUI();


        ((TextView)findViewById(R.id.tv_as_guest)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoTo.startWithFinish(LoginActivity.this, UserMainActivity.class);
            }
        });
    }

    private void initUI() {
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        cb_remember = findViewById(R.id.cb_remember);
        tv_forgot_password = findViewById(R.id.tv_forgot_password);
        tv_register = findViewById(R.id.tv_register);
        btn_login = findViewById(R.id.btn_login);
        tv_forgot_password.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        if (SessionManager.isRemembered()){
            edt_email.setText(SessionManager.getEmail());
            edt_password.setText(SessionManager.getPassword());
            cb_remember.setChecked(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_forgot_password: {
                GoTo.startWithFinish(LoginActivity.this, ForgotPasswordActivity.class);
            }
            break;
            case R.id.tv_register: {
                GoTo.startWithFinish(LoginActivity.this, RegisterActivity.class);
            }
            break;
            case R.id.btn_login: {
                if (Validator.isEmpty(edt_email)) {
                    Validator.setError(edt_email, "Enter Email");
                } else if (Validator.isNotEmail(edt_email)) {
                    Validator.setError(edt_email, "Enter Valid Email");
                } else if (Validator.isEmpty(edt_password)) {
                    Validator.setError(edt_password, "Enter Password");
                } else {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("action","login");
                    param.put("user_token",SessionManager.getFCM_TOKEN());
                    param.put("email",Validator.getText(edt_email));
                    param.put("password",Validator.getText(edt_password));
                    communication.callPOST(param);
                }
            } break;
        }
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {

        if (tag.equals("login")){
            try {
                SessionManager.setLogged(true);
                SessionManager.setPassword(Validator.getText(edt_password));
                SessionManager.setRemembered(cb_remember.isChecked());
                SessionManager.setEncryptedID(jsonObject.getString("encryptedId"));
                SessionManager.setToken(jsonObject.getString("token"));
                SessionManager.setMinPax(jsonObject.getInt("min_pax"));
                JSONObject data = jsonObject.getJSONObject("data");
                SessionManager.setName(data.getString("name"));
                SessionManager.setEmail(data.getString("email"));
                SessionManager.setRole(data.getString("role"));
                SessionManager.setID(data.getString("id"));
                SessionManager.setDOB(data.getString("date_of_birth"));
                SessionManager.setCountry(data.getString("country"));
                SessionManager.setLanguage(data.getString("language"));

                if (SessionManager.getRole().equals("Vendor")){
                    SessionManager.setVenueID(data.optString("venue_id"));
                    SessionManager.setVenueType(data.optString("venue_type"));
                    SessionManager.setVendorExpiryDate(jsonObject.getString("expire_date"));
                    SessionManager.setVendorSubscriptionType(jsonObject.getString("subscription_type"));
                    SessionManager.setVendorIsSubscription(jsonObject.getInt("is_subscribed"));
                    SessionManager.setVendorSubscriptionStatus(jsonObject.getString("subscription_status"));
                    SessionManager.ProfilePasswordSet(jsonObject.getInt("profile_password_set"));
                    SessionManager.setSlug(data.getString("venue_slug"));
                    SessionManager.setDepositOption(data.getString("deposit_option"));
                    SessionManager.setDepositPercentage(data.getString("deposit_percentage"));
                }
                Resources resources = getResources();
                DisplayMetrics dm = resources.getDisplayMetrics();
                Configuration config = resources.getConfiguration();
                config.setLocale(new Locale(SessionManager.getLanguage().toLowerCase()));
                resources.updateConfiguration(config, dm);
                if (SessionManager.getRole().equals("Admin")){
                    GoTo.startWithFinish(LoginActivity.this, AdminMainActivity.class);
                }else if (SessionManager.getRole().equals("Vendor")){
                    GoTo.startWithFinish(LoginActivity.this, VendorMainActivity.class);
                }else {
                    GoTo.startWithFinish(LoginActivity.this, UserMainActivity.class);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }


    @Override
    protected void onResume() {
        SessionManager.setLogged(false);
        SessionManager.setVendorConfirmPassword(false);
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.setLocale(new Locale(SessionManager.getLanguage().toLowerCase()));
        resources.updateConfiguration(config, dm);
        super.onResume();
    }
}
