package com.vip.marrakech.admin.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.vip.marrakech.R;
import com.vip.marrakech.adapters.ItineraryDetailAdapter;
import com.vip.marrakech.admin.models.BookingModel;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.models.ItineryDetail.GetItineraryDayDetail;
import com.vip.marrakech.models.ItineryDetail.ItineriDetailModel;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminBookingActivity extends AppCompatActivity implements OnCallBackListener {

    private TextView tv_client, tv_group, tv_pax, tv_total_spend,tv_balance_to_pay,tv_deposit_paid;

    private RecyclerView rv_booking_detail;
    private List<GetItineraryDayDetail> list = new ArrayList<>();
    private ItineraryDetailAdapter adapter;
    private Communication communication;
    private Bundle bundle;
    private BookingModel model;
    private ItineriDetailModel detailModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_booking_detail);
        communication = new Communication(this,this);
        Toolbar toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
       toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(MenuItem item) {
               if (item.getItemId() == R.id.opt_edit){
                   Bundle bundle = new Bundle();
                   bundle.putSerializable("id",model.getItineraryEncryptedId());
                   bundle.putSerializable("data",detailModel);
                   GoTo.startWithExtra(AdminBookingActivity.this,EditIteniraryActivity.class,bundle);
               }
               return false;
           }
       });

        bundle = GoTo.getIntent(this);

        if (bundle != null) {
            initUI();

        }
    }

    private void getList() {
        HashMap<String, String> param = new HashMap<>();
        param.put("action", String.format("itinerary/%s", model.getItineraryEncryptedId()));
        communication.callGET(param);
    }

    @Override
    protected void onResume() {
        if (bundle.getSerializable("model") != null) {
            model = (BookingModel) bundle.getSerializable("model");
            getList();


        }
        super.onResume();
    }

    private void initUI() {

        tv_client = findViewById(R.id.tv_client);
        tv_group = findViewById(R.id.tv_group);
        tv_pax = findViewById(R.id.tv_pax);
        tv_total_spend = findViewById(R.id.tv_total_spend);
        tv_deposit_paid = findViewById(R.id.tv_deposit_paid);
        tv_balance_to_pay = findViewById(R.id.tv_balance_to_pay);
        rv_booking_detail = findViewById(R.id.rv_booking_detail);
        rv_booking_detail.setLayoutManager(new LinearLayoutManager(AdminBookingActivity.this));

        adapter = new ItineraryDetailAdapter(AdminBookingActivity.this, list);
        rv_booking_detail.setAdapter(adapter);
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals(String.format("itinerary/%s", model.getItineraryEncryptedId()))){
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                detailModel = new Gson().fromJson(data.toString(), ItineriDetailModel.class);
                Log.e("ID::::",detailModel.getPax());
                tv_client.setText(getResources().getString(R.string.cline_name_s,detailModel.getClientName()));
                tv_group.setText(getResources().getString(R.string.group_type_s,detailModel.getGroup()));
                tv_pax.setText(getResources().getString(R.string.pax_s,detailModel.getPax()));
                tv_total_spend.setText(String.format("%s", detailModel.getTotalSpend()));
                tv_deposit_paid.setText(String.format("%s", detailModel.getHolding_deposit()));
                tv_balance_to_pay.setText(String.format("%s", detailModel.getBalance()));
                list.clear();
                list.addAll(detailModel.getGetItineraryDayDetail());
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }
}
