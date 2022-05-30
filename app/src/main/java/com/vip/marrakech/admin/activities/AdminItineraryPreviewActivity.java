package com.vip.marrakech.admin.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.vip.marrakech.R;
import com.vip.marrakech.adapters.ItineraryDetailAdapter;
import com.vip.marrakech.admin.models.AdminItineraryModel;
import com.vip.marrakech.dialogs.ReasonDialog;
import com.vip.marrakech.helpers.GoTo;
import com.vip.marrakech.models.ItineryDetail.DayDetail;
import com.vip.marrakech.models.ItineryDetail.GetItineraryDayDetail;
import com.vip.marrakech.models.ItineryDetail.ItineriDetailModel;
import com.vip.marrakech.retrofit.Communication;
import com.vip.marrakech.retrofit.interfaces.OnCallBackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AdminItineraryPreviewActivity extends AppCompatActivity implements OnCallBackListener, Toolbar.OnMenuItemClickListener {

    private TextView tv_client, tv_group, tv_pax, tv_total_spend,tv_balance_to_pay,tv_deposit_paid;;

    private RecyclerView rv_booking_detail;
    private List<GetItineraryDayDetail> list = new ArrayList<>();
    private ItineraryDetailAdapter adapter;
    private Communication communication;
    private Bundle bundle;
    private ItineriDetailModel detailModel;
    private String id;
    private CheckBox cb_now, cb_date;
    private String commitStatus = "N";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary_itinerary_preview);
        communication = new Communication(this, this);
        Toolbar toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        toolBar.setOnMenuItemClickListener(this);


        Button btn_commit = findViewById(R.id.btn_commit);
        Button btn_save = findViewById(R.id.btn_save);

        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cb_now.isChecked() && !cb_date.isChecked()){
                    Toast.makeText(AdminItineraryPreviewActivity.this, "Select Notification Time", Toast.LENGTH_SHORT).show();
                }else {
                    commitStatus = "Y";
                    commit("Y");
                }
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (!cb_now.isChecked() && !cb_date.isChecked()){
                    Toast.makeText(AdminItineraryPreviewActivity.this, "Select Notification Time", Toast.LENGTH_SHORT).show();
                }else {

                }*/  commitStatus = "N";
                commit("N");
            }
        });
    }


    private void slectDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(AdminItineraryPreviewActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        DecimalFormat formatter = new DecimalFormat("00");
                        String date = String.format(Locale.ENGLISH, "%d-%s-%s", year, formatter.format(month + 1), formatter.format(day));
                        cb_date.setText(date);

                        selectTime();
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void selectTime() {

        // TODO Auto-generated method stub
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(AdminItineraryPreviewActivity.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        DecimalFormat formatter = new DecimalFormat("00");
                        cb_date.append(String.format(Locale.ENGLISH, " %s:%s:00", formatter.format(hourOfDay), formatter.format(minute)));

                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }

    private void getList() {
        HashMap<String, String> param = new HashMap<>();
        param.put("action", String.format("itinerary/preview/%s", id));
        communication.callGET(param);
    }


    private void commit(String status) {
        HashMap<String, String> param = new HashMap<>();
        param.put("action", "itinerary/commit");
        param.put("itinerary_id", id);
        param.put("notification_sent", cb_now.isChecked() ? "1" : "2");
        if (cb_date.isChecked()) {
            param.put("notification_date_time", cb_date.getText().toString());
        }
        param.put("status", status);
        communication.callPOST(param);
    }

    private void initUI() {

        cb_now = findViewById(R.id.cb_now);
        cb_date = findViewById(R.id.cb_date);
        tv_client = findViewById(R.id.tv_client);
        tv_group = findViewById(R.id.tv_group);
        tv_pax = findViewById(R.id.tv_pax);
        tv_total_spend = findViewById(R.id.tv_total_spend);
        tv_balance_to_pay = findViewById(R.id.tv_balance_to_pay);
        tv_deposit_paid = findViewById(R.id.tv_deposit_paid);
        rv_booking_detail = findViewById(R.id.rv_booking_detail);
        rv_booking_detail.setLayoutManager(new LinearLayoutManager(AdminItineraryPreviewActivity.this));

        adapter = new ItineraryDetailAdapter(AdminItineraryPreviewActivity.this, list);
        rv_booking_detail.setAdapter(adapter);


        cb_now.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_date.setChecked(false);
                    cb_date.setText(getResources().getString(R.string.select_date_and_time));
                }
            }
        });
        cb_date.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_now.setChecked(false);
                    slectDate();
                }
            }
        });
    }

    @Override
    public void OnCallBackSuccess(String tag, JSONObject jsonObject) {
        if (tag.equals(String.format("itinerary/preview/%s", id))) {
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                detailModel = new Gson().fromJson(data.toString(), ItineriDetailModel.class);
                Log.e("ID::::", detailModel.getPax());
                tv_client.setText(getResources().getString(R.string.cline_name_s, detailModel.getClientName()));
                tv_group.setText(getResources().getString(R.string.group_type_s, detailModel.getGroup()));
                tv_pax.setText(getResources().getString(R.string.pax_s, detailModel.getPax()));
                tv_total_spend.setText(String.format("%s", detailModel.getTotalSpend()));
                tv_deposit_paid.setText(String.format("%s", detailModel.getHolding_deposit()));
                tv_balance_to_pay.setText(String.format("%s", detailModel.getBalance()));
                list.clear();
                list.addAll(detailModel.getGetItineraryDayDetail());
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (tag.equals("itinerary/commit")) {
            if(commitStatus.equals("Y")){
                ReasonDialog reasonDialog = new ReasonDialog();
                reasonDialog.setCancelable(false);
                reasonDialog.setListener( jsonObject.optString("msg"), new ReasonDialog.OnClickListener() {
                    @Override
                    public void onOkClick() {
                        Bundle bundle = new Bundle();
                        bundle.putInt("page",2);
                        GoTo.startWithExtraClearTop(AdminItineraryPreviewActivity.this,AdminMainActivity.class,bundle);
                    }
                });
                reasonDialog.show(getSupportFragmentManager(),"reason");
            }else{
                Toast.makeText(AdminItineraryPreviewActivity.this, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putInt("page",2);
                GoTo.startWithExtraClearTop(AdminItineraryPreviewActivity.this,AdminMainActivity.class,bundle);
            }

        }
    }

    @Override
    public void OnCallBackError(String tag, String error) {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.opt_edit) {
            onBackPressed();
        }
        return false;
    }


    @Override
    protected void onResume() {
        bundle = GoTo.getIntent(this);

        if (bundle != null) {
            initUI();
            if (bundle.getString("id") != null) {
                id = bundle.getString("id");
                getList();


            }
        }
        super.onResume();
    }
}
