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
import com.vip.marrakech.helpers.Validator;
import com.vip.marrakech.models.ItineryDetail.GetItineraryDayDetail;
import com.vip.marrakech.models.ItineryDetail.ItineriDetailModel;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AdminAdminBookingActivity extends AppCompatActivity implements OnCallBackListener {

    private TextView tv_client, tv_group, tv_pax, tv_total_spend,tv_table_no,tv_concierge,tv_date;

    private Communication communication;
    private Bundle bundle;
    private BookingModel model;
    private ItineriDetailModel detailModel;
    private TextView tv_day_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_admin_booking_detail);
        communication = new Communication(this,this);
        Toolbar toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        bundle = GoTo.getIntent(this);

        if (bundle != null) {
            initUI();

        }
    }

    private void getList() {
        HashMap<String, String> param = new HashMap<>();
        param.put("action", String.format("vendor/other/booking/detail/%s", model.getItineraryDayEncryptedId()));
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

        tv_date = findViewById(R.id.tv_date);
        tv_client = findViewById(R.id.tv_client);
        tv_concierge = findViewById(R.id.tv_concierge);
        tv_table_no = findViewById(R.id.tv_table_no);
        tv_day_detail = findViewById(R.id.tv_day_detail);
        tv_group = findViewById(R.id.tv_group);
        tv_pax = findViewById(R.id.tv_pax);
        tv_total_spend = findViewById(R.id.tv_total_spend);
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals(String.format("vendor/other/booking/detail/%s", model.getItineraryDayEncryptedId()))){
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                detailModel = new Gson().fromJson(data.toString(), ItineriDetailModel.class);
                Log.e("ID::::",detailModel.getPax());
                tv_client.setText(getResources().getString(R.string.cline_name_s,data.getString("first_name")+" "+data.getString("last_name")));
                tv_group.setText(getResources().getString(R.string.group_type_s,data.getString("group_type")));
                tv_pax.setText(getResources().getString(R.string.pax_s,data.getString("pax")));
                tv_concierge.setText(String.format(" : %s",data.getString("concierge_name")));
                tv_table_no.setText(String.format(" : %s",data.getString("table_no")));
                SimpleDateFormat apiFromate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
                SimpleDateFormat showFOrmate = new SimpleDateFormat("EEE, dd-MMM-yyyy", Locale.ENGLISH);
                try {
                    tv_date.setText(showFOrmate.format(apiFromate.parse(data.getString("booking_date_time"))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                tv_total_spend.setText(String.format("%s", data.getString("total_spend")));
                    tv_day_detail.setText(String.format("%s - %s - %s%s%s", Validator.getAmPM(data.getString("booking_time")),  model.getVendorName(),  data.getString("venue_type"),  data.getString("table_no").isEmpty()?"": String.format(" - %s",  data.getString("table_no")), data.getString("table_no").isEmpty()?"": String.format(" - %s",  data.getString("table_no"))));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }
}
