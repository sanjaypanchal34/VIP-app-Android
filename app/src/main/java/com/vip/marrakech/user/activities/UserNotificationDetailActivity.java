package com.vip.marrakech.user.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vip.marrakech.R;
import com.vip.marrakech.admin.models.AdminNotificationModel;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;

import org.json.JSONObject;

import java.util.HashMap;

public class UserNotificationDetailActivity extends AppCompatActivity implements View.OnClickListener, OnCallBackListener {

    private TextView tv_title;
    private Button btn_confirm, btn_on_time, btn_cancel, btn_15_min;
    private AdminNotificationModel model;
    private Communication communication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notification_detail);
        communication = new Communication(this, this);

        Bundle bundle = GoTo.getIntent(this);
        model = (AdminNotificationModel) bundle.getParcelable("data");

        initUI();
        if (model.getNotification_type().equalsIgnoreCase("upcoming_booking")) {
            btn_confirm.setVisibility(View.VISIBLE);
            btn_on_time.setVisibility(View.GONE);
            btn_cancel.setVisibility(View.VISIBLE);
            btn_15_min.setVisibility(View.GONE);
        } else {
            btn_confirm.setVisibility(View.GONE);
            btn_on_time.setVisibility(View.VISIBLE);
            btn_cancel.setVisibility(View.GONE);
            btn_15_min.setVisibility(View.VISIBLE);
        }

        tv_title.setText(model.getNotification_content());

        btn_confirm.setOnClickListener(this);
        btn_on_time.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_15_min.setOnClickListener(this);

    }

    private void initUI() {
        Toolbar toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tv_title = findViewById(R.id.tv_title);
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_on_time = findViewById(R.id.btn_on_time);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_15_min = findViewById(R.id.btn_15_min);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_confirm) {
            HashMap<String, String> param = new HashMap<>();
            param.put("action", "user/confirm/cancel/booking");
            param.put("notification_id", model.getNotification_id());
            param.put("is_confirm", "1");
            param.put("itinerary_day_id", model.getItinerary_day_id());
            param.put("itinerary_id", model.getItinerary_id());
            aleartDialog(param, "Are you sure you would like to confirm this?");
        } else if (id == R.id.btn_on_time) {
            HashMap<String, String> param = new HashMap<>();
            param.put("action", "user/ontime/late");
            param.put("notification_id", model.getNotification_id());
            param.put("venue_id", model.getVenue_id());
            param.put("time_value", "on_time");
            aleartDialog(param, "Are you sure About time?");
        } else if (id == R.id.btn_cancel) {
            HashMap<String, String> param = new HashMap<>();
            param.put("action", "user/confirm/cancel/booking");
            param.put("notification_id", model.getNotification_id());
            param.put("is_confirm", "0");
            param.put("itinerary_day_id", model.getItinerary_day_id());
            param.put("itinerary_id", model.getItinerary_id());
            aleartDialog(param, "Are sure want to reject this booking?");
        } else if (id == R.id.btn_15_min) {
            HashMap<String, String> param = new HashMap<>();
            param.put("action", "user/ontime/late");
            param.put("notification_id", model.getNotification_id());
            param.put("venue_id", model.getVenue_id());
            param.put("time_value", "15_mint_late");
            aleartDialog(param, "Are you sure About time?");
        }
    }

    private void aleartDialog(final HashMap<String, String> param, String s) {
        communication.callPOST(param);
    /*    new AlertDialog.Builder(UserNotificationDetailActivity.this)
                .setMessage(s)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        communication.callPOST(param);
                    }
                })

                .setNegativeButton(android.R.string.no, null)
                .show();*/
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equalsIgnoreCase("user/confirm/cancel/booking") || tag.equalsIgnoreCase("user/ontime/late")){
            Toast.makeText(UserNotificationDetailActivity.this, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }
}
