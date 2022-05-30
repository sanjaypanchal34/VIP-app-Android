package com.vip.marrakech.vendor.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.vip.marrakech.R;
import com.vip.marrakech.admin.adapters.AdminNotificationAdapter;
import com.vip.marrakech.admin.models.AdminNotificationModel;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;
import com.vip.marrakech.vendor.dialog.BookingDetailDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VendorNotificationActivity extends AppCompatActivity implements OnCallBackListener {

    private Toolbar toolBar;
    private RecyclerView rv_notification;
    private AdminNotificationAdapter adapter;
    private List<AdminNotificationModel> list = new ArrayList<>();
    private Communication communication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notification);
        communication = new Communication(this, this);


        initUI();


//        getList();
    }

    private void getList() {
        HashMap<String, String> param = new HashMap<>();
        param.put("action", "vendor/notifications");
        communication.callGET(param);
    }

    private void initUI() {
        toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rv_notification = findViewById(R.id.rv_notification);
        rv_notification.setLayoutManager(new LinearLayoutManager(VendorNotificationActivity.this));
        adapter = new AdminNotificationAdapter(VendorNotificationActivity.this, list);
        rv_notification.setAdapter(adapter);
        adapter.setListener(new AdminNotificationAdapter.OnNotificationClickListener() {
            @Override
            public void onNotificationClick(AdminNotificationModel model) {
                if (model.getView_detail().equals("1")) {
                  /*  Bundle bundle = new Bundle();
                    bundle.putParcelable("data",model);
                    GoTo.startWithExtra(VendorNotificationActivity.this,VendorNotificationDetailActivity.class,bundle);*/

                    BookingDetailDialog dialog = new BookingDetailDialog();
                    dialog.setData(model.getItinerary_day_id(), new BookingDetailDialog.ItemClickListener() {
                        @Override
                        public void onDismiss() {
                        }
                    });
                    dialog.show(getSupportFragmentManager(), "Booking");
                } else  if (model.getIs_read().equals("0")) {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("action", "notification/read");
                    param.put("notification_from", "Vendor");
                    param.put("notification_id", model.getNotification_id());
                    communication.callPOST(param);
                }
            }
        });

    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals("vendor/notifications")) {
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray admin_notification = data.getJSONArray("vendor_notification");
                list.clear();
                for (int i = 0; i < admin_notification.length(); i++) {
                    AdminNotificationModel model = new Gson().fromJson(admin_notification.getJSONObject(i).toString(), AdminNotificationModel.class);
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            getList();
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }

    @Override
    protected void onResume() {
        getList();
        registerReceiver(receiver,new IntentFilter("pushNotification"));
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getList();
        }
    };
}
