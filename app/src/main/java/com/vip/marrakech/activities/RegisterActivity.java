package com.vip.marrakech.activities;

import static com.github.willena.phoneinputview.CountryConfigurator.HintType.MOBILE;
import static com.github.willena.phoneinputview.CountryConfigurator.HintType.NONE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.willena.phoneinputview.CountryConfigurator;
import com.github.willena.phoneinputview.CountryInfo;
import com.github.willena.phoneinputview.PhoneInputView;
import com.github.willena.phoneinputview.events.OnCountryChangedListener;
import com.github.willena.phoneinputview.events.OnValidEntryListener;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, OnCallBackListener {

    private TextView tv_login;
    private EditText edt_f_name,
            edt_l_name, edt_email, edt_mobile, edt_password, edt_confirm_password;
    private Button btn_register;
    private Communication communication;
    private TextView tv_country,tv_dob;
    private String date;
    private Calendar calendar;
    private String ipAddress = "";
    private String ipAddress6 = "";
    private PhoneInputView phoneView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        calendar = Calendar.getInstance();
        communication = new Communication(this, this);
        initUI();
         phoneView = (PhoneInputView) findViewById(R.id.phoneId);
        CountryConfigurator config = new CountryConfigurator();
        config.setDisplayFlag(true);
        config.setDisplayCountryCode(false);
        config.setDisplayDialingCode(false);
        config.setPhoneNumberHintType(NONE); //Set the phone number type that will be displayed as hint (MOBILE, FIXED, NONE)
        config.setDefaultCountry("GB"); //Set the default country that will be selected when loading
        phoneView.setConfig(config);

        phoneView.addOnValidEntryListener(new OnValidEntryListener() {
            @Override
            public void onValidEntry(Boolean valid) {

            }
        });

        phoneView.addOnCountryChangeListener(new OnCountryChangedListener() {
            @Override
            public void onCountryChange(String country) {
                tv_country.setText(new CountryPicker.Builder().build().getCountryByISO(country).getName());

            }
        });
        communication.GETIP();

        tv_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);


        tv_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountryPicker.Builder builder =
                        new CountryPicker.Builder().with(RegisterActivity.this).canSearch(true)
                                .listener(new OnCountryPickerListener() {
                                    @Override
                                    public void onSelectCountry(Country country) {
                                        tv_country.setText(country.getName());
                                    }
                                });
                builder.build().showDialog(RegisterActivity.this);
            }
        });

        tv_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog picker = new DatePickerDialog(RegisterActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                               // eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                DecimalFormat formatter = new DecimalFormat("00");
                                SimpleDateFormat showFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                                SimpleDateFormat yyyy_mm_dd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                date = String.format(Locale.ENGLISH,"%s-%s-%s", formatter.format(year),  formatter.format((monthOfYear + 1)), formatter.format(dayOfMonth));
                                tv_dob.setText(String.format(Locale.ENGLISH,"%s/%s/%s", formatter.format(dayOfMonth),  formatter.format((monthOfYear + 1)), formatter.format(year)));
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                picker.show();
               /* CalenderDialog dialog = new CalenderDialog();
                dialog.setMinDate(new Date());
                dialog.setListener(new OnCalenderListener() {
                    @Override
                    public void onDateSelect(MyDate mydate) {
                        SimpleDateFormat showFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                        SimpleDateFormat yyyy_mm_dd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        date = yyyy_mm_dd.format(mydate.getDate());
                        tv_dob.setText(showFormat.format(mydate.getDate()));
                    }
                });
                dialog.show(getSupportFragmentManager(), "date");*/
            }
        });
    }

    private void initUI() {
        tv_login = findViewById(R.id.tv_login);
        edt_f_name = findViewById(R.id.edt_f_name);
        edt_l_name = findViewById(R.id.edt_l_name);
        edt_email = findViewById(R.id.edt_email);
        edt_mobile = findViewById(R.id.edt_mobile);
        edt_password = findViewById(R.id.edt_password);
        edt_confirm_password = findViewById(R.id.edt_confirm_password);
        tv_country = findViewById(R.id.tv_country);
        tv_dob = findViewById(R.id.tv_dob);
        btn_register = findViewById(R.id.btn_register);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_login) {
            GoTo.startWithFinish(RegisterActivity.this, LoginActivity.class);
        } else {
            if (Validator.isEmpty(edt_f_name)) {
                Validator.setError(edt_f_name, "Enter First Name");
            } else if (Validator.isEmpty(edt_l_name)) {
                Validator.setError(edt_l_name, "Enter Last Name");
            } else if (Validator.isEmpty(edt_email)) {
                Validator.setError(edt_email, "Enter  Email");
            } else if (Validator.isNotEmail(edt_email)) {
                Validator.setError(edt_email, "Enter Valid Email");
            } else if (!phoneView.isValid2()) {
                Toast.makeText(this, "Enter Valid Contact Number", Toast.LENGTH_SHORT).show();
            } else if (Validator.isEmpty(edt_password)) {
                Validator.setError(edt_password, "Enter Password");
            } else if (Validator.isEmpty(edt_confirm_password)) {
                Validator.setError(edt_confirm_password, "Enter Confirm Password");
            } else if (!Validator.getText(edt_confirm_password).equals(Validator.getText(edt_password))) {
                Validator.setError(edt_confirm_password, "Confirm Password doesn't match");
            } /*else if (tv_dob.getText().toString().isEmpty()) {
                Toast.makeText(this, "Select Date of Birth", Toast.LENGTH_SHORT).show();
            }*/ /* else if (tv_country.getText().toString().isEmpty()) {
                Toast.makeText(this, "Select Country", Toast.LENGTH_SHORT).show();
            }*/ else {

                HashMap<String, String> param = new HashMap<>();
                param.put("action", "register");
                param.put("first_name", Validator.getText(edt_f_name));
                param.put("last_name", Validator.getText(edt_l_name));
                param.put("contact_number",phoneView.getFormatedNumber().replaceAll(" ","").replaceAll("-",""));
                param.put("email", Validator.getText(edt_email));
                param.put("password", Validator.getText(edt_password));
                param.put("c_password", Validator.getText(edt_confirm_password));
                param.put("country", tv_country.getText().toString());
                if(!ipAddress.isEmpty()) {
                    param.put("device_ip", ipAddress);
                }
                if(!ipAddress6.isEmpty()) {
                    param.put("device_ipv6", ipAddress6);
                }
               // param.put("date_of_birth", date);
                communication.callPOST(param);
            }
        }
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals("register")) {
            try {
                SessionManager.setEncryptedID(jsonObject.getString("encryptedId"));
                JSONObject data = jsonObject.getJSONObject("data");
                SessionManager.setName(data.getString("name"));
                SessionManager.setEmail(data.getString("email"));
                SessionManager.setMobile(phoneView.getFormatedNumber().replaceAll("-",""));
                SessionManager.setRole(data.getString("role"));
                SessionManager.setID(data.getString("id"));

                new AlertDialog.Builder(RegisterActivity.this)
                        .setMessage(jsonObject.optString("msg"))

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                GoTo.startWithFinish(RegisterActivity.this, EmailVerificationActivity2.class);
                            }
                        })

                        .show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(tag.equals("IP")){
            Log.e("IP::::",jsonObject.optString("ip"));
            ipAddress = jsonObject.optString("ip");
            communication.GETIP6();
        }else if(tag.equals("IP6")){
            Log.e("IP6::::",jsonObject.optString("ip"));
            ipAddress6 = jsonObject.optString("ip");
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }
}
