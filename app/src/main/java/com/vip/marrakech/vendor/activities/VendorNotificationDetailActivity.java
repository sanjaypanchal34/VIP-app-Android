package com.vip.marrakech.vendor.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vip.marrakech.R;
import com.vip.marrakech.admin.models.AdminNotificationModel;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.helpers.Validator;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class VendorNotificationDetailActivity extends AppCompatActivity implements OnCallBackListener {

    private TextView tv_client_name, tv_group, tv_pax, tv_date,tv_time;
    private Button btn_accept, btn_reject,btn_submit;
    private Communication communication;
    private AdminNotificationModel model;
    private LinearLayout ln_reason;
    private EditText edt_reason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_notification_detail);
        communication = new Communication(this, this);
        Bundle bundle = GoTo.getIntent(this);
        model = bundle.getParcelable("data");
        initUI();

        if (model != null) {
            HashMap<String, String> param = new HashMap<>();
            param.put("action", "vendor/notification/details");
            param.put("notification_id", model.getNotification_id());
            param.put("itinerary_id", model.getItinerary_id());
            param.put("itinerary_day_id", model.getItinerary_day_id());
            communication.callPOST(param);

         /*   if (model.getIs_read().equals("0")) {
                HashMap<String, String> param1 = new HashMap<>();
                param1.put("action", "notification/read");
                param1.put("notification_from", "Vendor");
                param1.put("notification_id", model.getNotification_id());
                communication.callPOST(param1);
            }*/
        }

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> param = new HashMap<>();
                param.put("action", "vendor/confirm/booking");
                param.put("notification_id", model.getNotification_id());
                param.put("itinerary_id", model.getItinerary_id());
                param.put("notification_from", "Vendor");
                param.put("itinerary_day_id", model.getItinerary_day_id());
                communication.callPOST(param);
            }
        });
        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ln_reason.setVisibility(View.VISIBLE);
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validator.isEmpty(edt_reason)){
                    Validator.setError(edt_reason,"Enter Reason");
                }else {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("action", "vendor/reject/reason");
                    param.put("notification_id", model.getNotification_id());
                    param.put("itinerary_id", model.getItinerary_id());
                    param.put("itinerary_day_id", model.getItinerary_day_id());
                    param.put("reason_for_reject", Validator.getText(edt_reason));
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
                onBackPressed();
            }
        });
        ln_reason = findViewById(R.id.ln_reason);

        edt_reason = findViewById(R.id.edt_reason);
        btn_submit = findViewById(R.id.btn_submit);
        tv_client_name = findViewById(R.id.tv_client_name);
        tv_group = findViewById(R.id.tv_group);
        tv_pax = findViewById(R.id.tv_pax);
        tv_date = findViewById(R.id.tv_date);
        tv_time = findViewById(R.id.tv_time);
        btn_accept = findViewById(R.id.btn_accept);
        btn_reject = findViewById(R.id.btn_reject);
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals("vendor/notification/details")) {
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONObject booking_details = data.getJSONObject("booking_details");
                tv_client_name.setText(String.format("Client Name : %s", booking_details.getString("client_name")));
                tv_group.setText(String.format("Group Type : %s", booking_details.getString("group")));
                tv_pax.setText(String.format("Group Size : %s", booking_details.getString("pax")));
                tv_date.setText(String.format("Date : %s", booking_details.getString("start_date")));
                tv_time.setText(String.format("Time : %s", booking_details.getString("start_time")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (tag.equals("vendor/confirm/booking") || tag.equals("vendor/reject/reason")) {
            String msg = jsonObject.optString("msg");
            new AlertDialog.Builder(VendorNotificationDetailActivity.this)
                    .setCancelable(false)
                    .setMessage(msg)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }
}
